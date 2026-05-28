package com.tajweed.ustoz.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.QuranRepository
import com.tajweed.ustoz.data.repository.TajweedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeRuleItem(
    val ruleId: Int,
    val nameUz: String,
    val category: String,
    val practiceCount: Int,
    val bestScore: Float
)

data class PracticeSurahItem(
    val surahNumber: Int,
    val surahName: String,
    val surahNameUz: String
)

data class PracticeListUiState(
    val rules: List<PracticeRuleItem> = emptyList(),
    val surahs: List<PracticeSurahItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PracticeListViewModel @Inject constructor(
    private val tajweedRepository: TajweedRepository,
    private val quranRepository: QuranRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeListUiState())
    val uiState: StateFlow<PracticeListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load rules with progress
            val rules = tajweedRepository.getAllRules().firstOrNull() ?: emptyList()
            val progressList = progressRepository.getProgress().firstOrNull() ?: emptyList()
            val progressMap = progressList.associateBy { it.ruleId }

            val ruleItems = rules.map { rule ->
                val progress = progressMap[rule.id]
                PracticeRuleItem(
                    ruleId = rule.id,
                    nameUz = rule.nameUz,
                    category = rule.category,
                    practiceCount = progress?.totalAttempts ?: 0,
                    bestScore = progress?.practiceScore ?: 0f
                )
            }

            // Load surahs (distinct)
            val ayahs = quranRepository.getAllAyahs().firstOrNull() ?: emptyList()
            val surahItems = ayahs
                .distinctBy { it.surahNumber }
                .map { ayah ->
                    PracticeSurahItem(
                        surahNumber = ayah.surahNumber,
                        surahName = ayah.surahName,
                        surahNameUz = ayah.surahNameUz
                    )
                }

            _uiState.value = PracticeListUiState(
                rules = ruleItems,
                surahs = surahItems,
                isLoading = false
            )
        }
    }
}
