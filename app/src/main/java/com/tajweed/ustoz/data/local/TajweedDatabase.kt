package com.tajweed.ustoz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tajweed.ustoz.data.model.QuranAyah
import com.tajweed.ustoz.data.model.RecordingResult
import com.tajweed.ustoz.data.model.TajweedRule
import com.tajweed.ustoz.data.model.UserProgress

@Database(
    entities = [
        TajweedRule::class,
        QuranAyah::class,
        UserProgress::class,
        RecordingResult::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TajweedDatabase : RoomDatabase() {

    abstract fun tajweedRuleDao(): TajweedRuleDao

    abstract fun quranAyahDao(): QuranAyahDao

    abstract fun userProgressDao(): UserProgressDao

    abstract fun recordingResultDao(): RecordingResultDao
}
