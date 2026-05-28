package com.tajweed.ustoz.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.model.TajweedRule
import com.tajweed.ustoz.data.model.UserProgress
import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val totalRules: Int = 0,
    val completedRules: Int = 0,
    val overallScore: Float = 0f,
    val streak: Int = 0,
    val recentResults: List<UserProgress> = emptyList(),
    val lastLesson: TajweedRule? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val tajweedRepository: TajweedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            combine(
                tajweedRepository.getAllRules(),
                progressRepository.getProgress()
            ) { rules, progressList ->
                val completedCount = progressList.count { it.lessonCompleted }
                val averageScore = if (progressList.isNotEmpty()) {
                    progressList.map { it.practiceScore }.average().toFloat()
                } else 0f
                val recent = progressList
                    .sortedByDescending { it.lastPracticeDate }
                    .take(5)
                val lastPracticed = progressList
                    .maxByOrNull { it.lastPracticeDate }
                val lastRule = lastPracticed?.let { progress ->
                    rules.find { it.id == progress.ruleId }
                }

                HomeUiState(
                    totalRules = rules.size,
                    completedRules = completedCount,
                    overallScore = averageScore,
                    streak = calculateStreak(progressList),
                    recentResults = recent,
                    lastLesson = lastRule ?: rules.firstOrNull(),
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun calculateStreak(progressList: List<UserProgress>): Int {
        if (progressList.isEmpty()) return 0
        val today = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        var streak = 0
        var checkDate = today
        val sortedDates = progressList
            .map { it.lastPracticeDate }
            .filter { it > 0 }
            .distinct()
            .sortedDescending()

        for (date in sortedDates) {
            if (checkDate - date <= oneDayMs) {
                streak++
                checkDate = date
            } else {
                break
            }
        }
        return streak
    }
}
