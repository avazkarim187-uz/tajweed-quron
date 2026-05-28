package com.tajweed.ustoz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_ayahs")
data class QuranAyah(
    @PrimaryKey
    val id: Int,
    val surahNumber: Int,
    val surahName: String,
    val surahNameUz: String,
    val ayahNumber: Int,
    val arabicText: String,
    val transliterationUz: String,
    val translationUz: String,
    val tajweedRuleIds: String,
    val page: Int
)
