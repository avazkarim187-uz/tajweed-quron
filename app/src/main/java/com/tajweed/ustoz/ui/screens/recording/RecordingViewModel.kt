package com.tajweed.ustoz.ui.screens.recording

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.model.RecordingResult
import com.tajweed.ustoz.data.model.UserProgress
import com.tajweed.ustoz.data.remote.WhisperRepository
import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.QuranRepository
import com.tajweed.ustoz.data.repository.RecordingRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import com.tajweed.ustoz.util.AudioRecorder
import com.tajweed.ustoz.util.TajweedAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

enum class RecordingState {
    Idle, Recording, Processing, Done, Error
}

data class RecordingUiState(
    val state: RecordingState = RecordingState.Idle,
    val targetText: String = "",
    val targetTransliteration: String = "",
    val recordingDuration: Long = 0L,
    val error: String? = null,
    val resultId: Int? = null
)

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val whisperRepository: WhisperRepository,
    private val recordingRepository: RecordingRepository,
    private val tajweedAnalyzer: TajweedAnalyzer,
    private val quranRepository: QuranRepository,
    private val tajweedRepository: TajweedRepository,
    private val progressRepository: ProgressRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState.asStateFlow()

    private var audioRecorder: AudioRecorder? = null
    private var outputFile: File? = null
    private var timerJob: Job? = null
    private var currentAyahId: Int? = null
    private var currentRuleId: Int? = null
    private var ruleIds: List<Int> = emptyList()

    fun loadTarget(ayahId: Int?, ruleId: Int?) {
        currentAyahId = ayahId
        currentRuleId = ruleId

        viewModelScope.launch {
            if (ayahId != null) {
                val ayah = quranRepository.getAyahById(ayahId).firstOrNull()
                if (ayah != null) {
                    ruleIds = ayah.tajweedRuleIds.split(",")
                        .mapNotNull { it.trim().toIntOrNull() }
                    _uiState.value = _uiState.value.copy(
                        targetText = ayah.arabicText,
                        targetTransliteration = ayah.transliterationUz
                    )
                }
            } else if (ruleId != null) {
                val rule = tajweedRepository.getRuleById(ruleId).firstOrNull()
                if (rule != null) {
                    ruleIds = listOf(rule.id)
                    val exampleText = rule.arabicExample.split("|").firstOrNull()?.trim() ?: rule.arabicExample
                    _uiState.value = _uiState.value.copy(
                        targetText = exampleText,
                        targetTransliteration = rule.nameUz
                    )
                }
            }
        }
    }

    fun startRecording() {
        val context = getApplication<Application>()
        val file = File(context.cacheDir, "recording_${System.currentTimeMillis()}.m4a")
        outputFile = file

        try {
            audioRecorder = AudioRecorder(context).apply {
                startRecording(file)
            }
            _uiState.value = _uiState.value.copy(
                state = RecordingState.Recording,
                recordingDuration = 0L,
                error = null
            )
            startTimer()
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                state = RecordingState.Error,
                error = "Yozib olishni boshlab bo'lmadi: ${e.message}"
            )
        }
    }

    fun stopRecording() {
        timerJob?.cancel()
        try {
            audioRecorder?.stopRecording()
            audioRecorder = null
            analyzeRecording()
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                state = RecordingState.Error,
                error = "Yozib olishni to'xtatib bo'lmadi: ${e.message}"
            )
        }
    }

    private fun analyzeRecording() {
        _uiState.value = _uiState.value.copy(state = RecordingState.Processing)

        viewModelScope.launch {
            val file = outputFile ?: run {
                _uiState.value = _uiState.value.copy(
                    state = RecordingState.Error,
                    error = "Audio fayl topilmadi"
                )
                return@launch
            }

            val transcriptionResult = whisperRepository.transcribeAudio(file)
            transcriptionResult.fold(
                onSuccess = { transcription ->
                    val expectedText = _uiState.value.targetText
                    val analysisResult = tajweedAnalyzer.analyzeTranscription(
                        expected = expectedText,
                        actual = transcription,
                        ruleIds = ruleIds
                    )

                    val errorsJson = analysisResult.errors.joinToString(";") { error ->
                        "${error.errorType}|${error.position}|${error.expected}|${error.actual}|${error.ruleName}|${error.severity}"
                    }

                    val recordingResult = RecordingResult(
                        ruleId = currentRuleId,
                        ayahId = currentAyahId,
                        audioPath = file.absolutePath,
                        transcription = transcription,
                        score = analysisResult.score,
                        errors = errorsJson,
                        timestamp = System.currentTimeMillis()
                    )

                    val savedId = recordingRepository.saveResult(recordingResult)

                    // Update user progress if rule-based
                    currentRuleId?.let { ruleId ->
                        val existing = progressRepository.getProgressByRule(ruleId).firstOrNull()
                        val progress = existing?.copy(
                            practiceScore = maxOf(existing.practiceScore, analysisResult.score),
                            lastPracticeDate = System.currentTimeMillis(),
                            totalAttempts = existing.totalAttempts + 1,
                            successfulAttempts = if (analysisResult.score >= 70f)
                                existing.successfulAttempts + 1 else existing.successfulAttempts
                        ) ?: UserProgress(
                            ruleId = ruleId,
                            practiceScore = analysisResult.score,
                            lastPracticeDate = System.currentTimeMillis(),
                            totalAttempts = 1,
                            successfulAttempts = if (analysisResult.score >= 70f) 1 else 0
                        )
                        progressRepository.updateProgress(progress)
                    }

                    _uiState.value = _uiState.value.copy(
                        state = RecordingState.Done,
                        resultId = savedId.toInt()
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        state = RecordingState.Error,
                        error = "Tahlil xatosi: ${error.message}"
                    )
                }
            )
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            var elapsed = 0L
            while (true) {
                delay(1000)
                elapsed++
                _uiState.value = _uiState.value.copy(recordingDuration = elapsed)
            }
        }
    }

    fun resetState() {
        timerJob?.cancel()
        audioRecorder?.release()
        audioRecorder = null
        _uiState.value = RecordingUiState()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioRecorder?.release()
    }
}
