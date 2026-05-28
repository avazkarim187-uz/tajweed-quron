package com.tajweed.ustoz.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tajweed.ustoz.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val database: Provider<TajweedDatabase>,
    @ApplicationScope private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        val tajweedRuleDao = database.get().tajweedRuleDao()
        val quranAyahDao = database.get().quranAyahDao()

        // Insert all tajweed rules
        tajweedRuleDao.insertAll(TajweedData.getDefaultRules())

        // Insert all Quran ayahs
        quranAyahDao.insertAll(QuranData.getDefaultAyahs())
    }
}
