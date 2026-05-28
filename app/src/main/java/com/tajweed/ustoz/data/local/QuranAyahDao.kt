package com.tajweed.ustoz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tajweed.ustoz.data.model.QuranAyah
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranAyahDao {

    @Query("SELECT * FROM quran_ayahs")
    fun getAllAyahs(): Flow<List<QuranAyah>>

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getAyahsBySurah(surahNumber: Int): Flow<List<QuranAyah>>

    @Query("SELECT * FROM quran_ayahs WHERE id = :id")
    fun getAyahById(id: Int): Flow<QuranAyah?>

    @Query("SELECT * FROM quran_ayahs WHERE ayahNumber = 1 ORDER BY surahNumber ASC")
    fun getSurahList(): Flow<List<QuranAyah>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ayahs: List<QuranAyah>)
}
