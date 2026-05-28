package com.tajweed.ustoz.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.model.RecordingResult
import com.tajweed.ustoz.data.model.TajweedRule
import com.tajweed.ustoz.data.model.UserProgress
import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.RecordingRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class RuleProgress(
    val rule: TajweedRule,
    val progress: UserProgress?,
    val scorePercentage: Float
)

data class ProgressUiState(
    val overallScore: Float = 0f,
    val totalSessions: Int = 0,
    val successRate: Float = 0f,
    val streak: Int = 0,
    val ruleProgressList: List<RuleProgress> = emptyList(),
    val weeklyActivity: List<Boolean> = List(7) { false },
    val recentResults: List<RecordingResult> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val recordingRepository: RecordingRepository,
    private val tajweedRepository: TajweedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            combine(
                progressRepository.getProgress(),
                tajweedRepository.getAllRules(),
                recordingRepository.getRecentResults(10)
            ) { progressList, rules, recentResults ->
                Triple(progressList, rules, recentResults)
            }.collect { (progressList, rules, recentResults) ->
                val ruleProgressList = rules.map { rule ->
                    val progress = progressList.find { it.ruleId == rule.id }
                    val score = progress?.practiceScore ?: 0f
                    RuleProgress(
                        rule = rule,
                        progress = progress,
                        scorePercentage = score
                    )
                }

                val totalSessions = progressList.sumOf { it.totalAttempts }
                val totalSuccessful = progressList.sumOf { it.successfulAttempts }
                val successRate = if (totalSessions > 0) {
                    (totalSuccessful.toFloat() / totalSessions) * 100f
                } else 0f

                val overallScore = if (progressList.isNotEmpty()) {
                    progressList.map { it.practiceScore }.average().toFloat()
                } else 0f

                val streak = calculateStreak(recentResults)
                val weeklyActivity = calculateWeeklyActivity(recentResults)

                _uiState.value = ProgressUiState(
                    overallScore = overallScore,
                    totalSessions = totalSessions,
                    successRate = successRate,
                    streak = streak,
                    ruleProgressList = ruleProgressList,
                    weeklyActivity = weeklyActivity,
                    recentResults = recentResults,
                    isLoading = false
                )
            }
        }
    }

    private fun calculateStreak(results: List<RecordingResult>): Int {
        if (results.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        val practiceDays = results.map { result ->
            val cal = Calendar.getInstance().apply { timeInMillis = result.timestamp }
            Pair(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR))
        }.distinct().sortedByDescending { it.second }

        var streak = 0
        var expectedDay = today
        var expectedYear = currentYear

        for ((year, day) in practiceDays) {
            if (year == expectedYear && (day == expectedDay || day == expectedDay - 1)) {
                streak++
                expectedDay = day - 1
                if (expectedDay < 1) {
                    expectedYear--
                    expectedDay = 365
                }
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateWeeklyActivity(results: List<RecordingResult>): List<Boolean> {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        val startOfWeek = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val weekDays = mutableListOf<Boolean>()
        for (i in 0 until 7) {
            val dayStart = Calendar.getInstance().apply {
                timeInMillis = startOfWeek.timeInMillis
                add(Calendar.DAY_OF_YEAR, i)
            }
            val dayEnd = Calendar.getInstance().apply {
                timeInMillis = dayStart.timeInMillis
                add(Calendar.DAY_OF_YEAR, 1)
            }
            val hasActivity = results.any { result ->
                result.timestamp >= dayStart.timeInMillis && result.timestamp < dayEnd.timeInMillis
            }
            weekDays.add(hasActivity)
        }
        return weekDays
    }
}
