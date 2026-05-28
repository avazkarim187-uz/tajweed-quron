package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.model.QuranAyah
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    fun getAllAyahs(): Flow<List<QuranAyah>>
    fun getAyahsBySurah(surahNumber: Int): Flow<List<QuranAyah>>
    fun getSurahList(): Flow<List<QuranAyah>>
    fun getAyahById(id: Int): Flow<QuranAyah?>
}
