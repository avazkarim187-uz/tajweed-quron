package com.tajweed.ustoz.ui.screens.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.model.TajweedRule
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val tajweedRepository: TajweedRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ruleId: Int = savedStateHandle["ruleId"] ?: 0

    private val _rule = MutableStateFlow<TajweedRule?>(null)
    val rule: StateFlow<TajweedRule?> = _rule.asStateFlow()

    init {
        loadRule()
    }

    private fun loadRule() {
        viewModelScope.launch {
            tajweedRepository.getRuleById(ruleId).collect { tajweedRule ->
                _rule.value = tajweedRule
            }
        }
    }
}
