package com.tajweed.ustoz.ui.screens.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.repository.RecordingRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TajweedHighlightColor {
    CORRECT, ERROR, WARNING, NEUTRAL
}

data class ColoredSegment(
    val text: String,
    val color: TajweedHighlightColor,
    val errorInfo: FeedbackError? = null
)

data class FeedbackError(
    val errorType: String,
    val position: Int,
    val expected: String,
    val actual: String,
    val ruleName: String,
    val severity: String,
    val ruleId: Int? = null,
    val explanation: String = ""
)

data class FeedbackUiState(
    val score: Float = 0f,
    val errors: List<FeedbackError> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val originalText: String = "",
    val coloredSegments: List<ColoredSegment> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    private val tajweedRepository: TajweedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun loadResult(resultId: Int) {
        viewModelScope.launch {
            val result = recordingRepository.getResultById(resultId).firstOrNull()

            if (result == null) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }

            val errors = parseErrors(result.errors)
            val suggestions = generateSuggestions(result.score, errors)
            val coloredSegments = buildColoredSegments(result.transcription, errors)

            // Map rule IDs to names if the error has a ruleId
            val enrichedErrors = errors.map { error ->
                if (error.ruleId != null) {
                    val rule = tajweedRepository.getRuleById(error.ruleId).firstOrNull()
                    error.copy(
                        ruleName = rule?.nameUz ?: error.ruleName,
                        explanation = rule?.description ?: ""
                    )
                } else {
                    error
                }
            }

            _uiState.value = FeedbackUiState(
                score = result.score,
                errors = enrichedErrors,
                suggestions = suggestions,
                originalText = result.transcription,
                coloredSegments = coloredSegments,
                isLoading = false
            )
        }
    }

    private fun parseErrors(errorsString: String): List<FeedbackError> {
        if (errorsString.isBlank()) return emptyList()

        return errorsString.split(";").mapNotNull { errorStr ->
            val parts = errorStr.split("|")
            if (parts.size >= 6) {
                FeedbackError(
                    errorType = parts[0],
                    position = parts[1].toIntOrNull() ?: 0,
                    expected = parts[2],
                    actual = parts[3],
                    ruleName = parts[4],
                    severity = parts[5],
                    ruleId = parts.getOrNull(6)?.toIntOrNull()
                )
            } else null
        }
    }

    private fun generateSuggestions(score: Float, errors: List<FeedbackError>): List<String> {
        val suggestions = mutableListOf<String>()

        if (score >= 80f) {
            suggestions.add("Juda yaxshi natija! Shu tarzda davom eting.")
        } else if (score >= 60f) {
            suggestions.add("Yaxshi harakat! Bir oz ko'proq mashq qilsangiz, mukammal bo'ladi.")
        } else {
            suggestions.add("Tajvid qoidalariga e'tibor bering va sekinroq o'qing.")
        }

        val highSeverityCount = errors.count { it.severity == "high" }
        if (highSeverityCount > 0) {
            suggestions.add("$highSeverityCount ta jiddiy xato aniqlandi. Tegishli darslarni ko'rib chiqing.")
        }

        if (errors.any { it.errorType == "mismatch" }) {
            suggestions.add("Harflarni to'g'ri talaffuz qilishga e'tibor bering.")
        }

        return suggestions
    }

    private fun buildColoredSegments(
        text: String,
        errors: List<FeedbackError>
    ): List<ColoredSegment> {
        if (text.isEmpty()) return emptyList()
        if (errors.isEmpty()) {
            return listOf(ColoredSegment(text, TajweedHighlightColor.CORRECT))
        }

        val segments = mutableListOf<ColoredSegment>()
        val errorPositions = errors.associate { it.position to it }
        var currentIndex = 0

        val sortedPositions = errorPositions.keys.sorted()

        for (pos in sortedPositions) {
            if (pos > currentIndex && pos <= text.length) {
                // Add correct segment before error
                segments.add(
                    ColoredSegment(
                        text = text.substring(currentIndex, pos),
                        color = TajweedHighlightColor.CORRECT
                    )
                )
            }

            if (pos < text.length) {
                val error = errorPositions[pos]!!
                val endPos = (pos + 1).coerceAtMost(text.length)
                val highlightColor = when (error.severity) {
                    "high" -> TajweedHighlightColor.ERROR
                    "medium" -> TajweedHighlightColor.WARNING
                    else -> TajweedHighlightColor.WARNING
                }
                segments.add(
                    ColoredSegment(
                        text = text.substring(pos, endPos),
                        color = highlightColor,
                        errorInfo = error
                    )
                )
                currentIndex = endPos
            }
        }

        // Add remaining correct text
        if (currentIndex < text.length) {
            segments.add(
                ColoredSegment(
                    text = text.substring(currentIndex),
                    color = TajweedHighlightColor.CORRECT
                )
            )
        }

        return segments
    }
}
