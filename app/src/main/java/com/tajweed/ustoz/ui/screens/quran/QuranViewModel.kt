package com.tajweed.ustoz.ui.screens.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajweed.ustoz.data.model.QuranAyah
import com.tajweed.ustoz.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SurahInfo(
    val surahNumber: Int,
    val surahName: String,
    val surahNameUz: String,
    val ayahCount: Int,
    val page: Int
)

data class QuranUiState(
    val surahs: List<SurahInfo> = emptyList(),
    val currentSurahAyahs: List<QuranAyah> = emptyList(),
    val selectedAyahId: Int? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val quranRepository: QuranRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    init {
        loadSurahs()
    }

    private fun loadSurahs() {
        viewModelScope.launch {
            quranRepository.getAllAyahs().collect { ayahs ->
                val surahInfoList = ayahs
                    .groupBy { it.surahNumber }
                    .map { (surahNumber, surahAyahs) ->
                        SurahInfo(
                            surahNumber = surahNumber,
                            surahName = surahAyahs.first().surahName,
                            surahNameUz = surahAyahs.first().surahNameUz,
                            ayahCount = surahAyahs.size,
                            page = surahAyahs.first().page
                        )
                    }
                    .sortedBy { it.surahNumber }
                _uiState.value = _uiState.value.copy(
                    surahs = surahInfoList,
                    isLoading = false
                )
            }
        }
    }

    fun loadSurah(surahNumber: Int) {
        viewModelScope.launch {
            quranRepository.getAyahsBySurah(surahNumber).collect { ayahs ->
                _uiState.value = _uiState.value.copy(
                    currentSurahAyahs = ayahs,
                    selectedAyahId = null
                )
            }
        }
    }

    fun selectAyahForPractice(ayahId: Int) {
        _uiState.value = _uiState.value.copy(
            selectedAyahId = if (_uiState.value.selectedAyahId == ayahId) null else ayahId
        )
    }
}
