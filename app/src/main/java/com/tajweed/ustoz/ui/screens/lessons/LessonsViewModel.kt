package com.tajweed.ustoz.ui.screens.lessons

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

data class LessonWithProgress(
    val rule: TajweedRule,
    val progress: UserProgress? = null
)

data class LessonsUiState(
    val lessons: List<LessonWithProgress> = emptyList(),
    val filteredLessons: List<LessonWithProgress> = emptyList(),
    val selectedCategory: String = "Barchasi",
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val tajweedRepository: TajweedRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonsUiState())
    val uiState: StateFlow<LessonsUiState> = _uiState.asStateFlow()

    init {
        loadLessons()
    }

    private fun loadLessons() {
        viewModelScope.launch {
            combine(
                tajweedRepository.getAllRules(),
                progressRepository.getProgress()
            ) { rules, progressList ->
                val lessonsWithProgress = rules.map { rule ->
                    LessonWithProgress(
                        rule = rule,
                        progress = progressList.find { it.ruleId == rule.id }
                    )
                }
                lessonsWithProgress
            }.collect { lessons ->
                _uiState.value = _uiState.value.copy(
                    lessons = lessons,
                    filteredLessons = applyFilters(lessons, _uiState.value.selectedCategory, _uiState.value.searchQuery),
                    isLoading = false
                )
            }
        }
    }

    fun filterByCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredLessons = applyFilters(_uiState.value.lessons, category, _uiState.value.searchQuery)
        )
    }

    fun searchRules(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredLessons = applyFilters(_uiState.value.lessons, _uiState.value.selectedCategory, query)
        )
    }

    private fun applyFilters(
        lessons: List<LessonWithProgress>,
        category: String,
        query: String
    ): List<LessonWithProgress> {
        var filtered = lessons

        if (category != "Barchasi") {
            filtered = filtered.filter { it.rule.category == category }
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.rule.name.contains(query, ignoreCase = true) ||
                    it.rule.nameUz.contains(query, ignoreCase = true) ||
                    it.rule.description.contains(query, ignoreCase = true)
            }
        }

        return filtered
    }
}
