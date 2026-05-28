package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.local.QuranAyahDao
import com.tajweed.ustoz.data.model.QuranAyah
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val quranAyahDao: QuranAyahDao
) : QuranRepository {

    override fun getAllAyahs(): Flow<List<QuranAyah>> {
        return quranAyahDao.getAllAyahs()
    }

    override fun getAyahsBySurah(surahNumber: Int): Flow<List<QuranAyah>> {
        return quranAyahDao.getAyahsBySurah(surahNumber)
    }

    override fun getSurahList(): Flow<List<QuranAyah>> {
        return quranAyahDao.getSurahList()
    }

    override fun getAyahById(id: Int): Flow<QuranAyah?> {
        return quranAyahDao.getAyahById(id)
    }
}
