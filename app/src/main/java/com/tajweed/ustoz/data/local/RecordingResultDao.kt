package com.tajweed.ustoz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tajweed.ustoz.data.model.RecordingResult
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordingResultDao {

    @Query("SELECT * FROM recording_results ORDER BY timestamp DESC")
    fun getAll(): Flow<List<RecordingResult>>

    @Query("SELECT * FROM recording_results WHERE ruleId = :ruleId ORDER BY timestamp DESC")
    fun getByRule(ruleId: Int): Flow<List<RecordingResult>>

    @Query("SELECT * FROM recording_results WHERE ayahId = :ayahId ORDER BY timestamp DESC")
    fun getByAyah(ayahId: Int): Flow<List<RecordingResult>>

    @Insert
    suspend fun insert(result: RecordingResult)

    @Query("SELECT * FROM recording_results ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentResults(limit: Int): Flow<List<RecordingResult>>
}
