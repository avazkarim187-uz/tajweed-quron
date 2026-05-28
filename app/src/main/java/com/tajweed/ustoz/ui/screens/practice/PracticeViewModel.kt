package com.tajweed.ustoz.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.QuranRepository
import com.tajweed.ustoz.data.repository.RecordingRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeText(
    val arabicText: String,
    val source: String,
    val ayahId: Int? = null
)

data class PracticeUiState(
    val ruleName: String = "",
    val ruleDescription: String = "",
    val ruleExplanation: String = "",
    val practiceTexts: List<PracticeText> = emptyList(),
    val currentIndex: Int = 0,
    val sessionAttempts: Int = 0,
    val sessionBestScore: Float = 0f,
    val overallProgress: Float = 0f,
    val isLoading: Boolean = true
)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val tajweedRepository: TajweedRepository,
    private val quranRepository: QuranRepository,
    private val progressRepository: ProgressRepository,
    private val recordingRepository: RecordingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    fun loadRule(ruleId: Int) {
        viewModelScope.launch {
            val rule = tajweedRepository.getRuleById(ruleId).firstOrNull()
            if (rule == null) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }

            val practiceTexts = mutableListOf<PracticeText>()

            // Add rule examples
            val examples = rule.arabicExample.split("|").map { it.trim() }
            examples.forEach { example ->
                if (example.isNotBlank()) {
                    practiceTexts.add(
                        PracticeText(
                            arabicText = example,
                            source = "Misol"
                        )
                    )
                }
            }

            // Add Quran ayahs that contain this rule
            val allAyahs = quranRepository.getAllAyahs().firstOrNull() ?: emptyList()
            val relevantAyahs = allAyahs.filter { ayah ->
                ayah.tajweedRuleIds.split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
                    .contains(ruleId)
            }.take(5)

            relevantAyahs.forEach { ayah ->
                practiceTexts.add(
                    PracticeText(
                        arabicText = ayah.arabicText,
                        source = "${ayah.surahNameUz} ${ayah.ayahNumber}-oyat",
                        ayahId = ayah.id
                    )
                )
            }

            // Load progress
            val progress = progressRepository.getProgressByRule(ruleId).firstOrNull()

            _uiState.value = PracticeUiState(
                ruleName = rule.nameUz,
                ruleDescription = rule.description,
                ruleExplanation = rule.explanation,
                practiceTexts = practiceTexts,
                sessionAttempts = progress?.totalAttempts ?: 0,
                sessionBestScore = progress?.practiceScore ?: 0f,
                overallProgress = progress?.practiceScore ?: 0f,
                isLoading = false
            )
        }
    }

    fun getNextPracticeItem(): PracticeText? {
        val texts = _uiState.value.practiceTexts
        if (texts.isEmpty()) return null
        val nextIndex = (_uiState.value.currentIndex + 1) % texts.size
        _uiState.value = _uiState.value.copy(currentIndex = nextIndex)
        return texts[nextIndex]
    }

    fun updateProgress(score: Float) {
        _uiState.value = _uiState.value.copy(
            sessionAttempts = _uiState.value.sessionAttempts + 1,
            sessionBestScore = maxOf(_uiState.value.sessionBestScore, score)
        )
    }
}
