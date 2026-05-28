package com.tajweed.ustoz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tajweed.ustoz.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgress>>

    @Query("SELECT * FROM user_progress WHERE ruleId = :ruleId")
    fun getProgressByRule(ruleId: Int): Flow<UserProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: UserProgress)

    @Query("UPDATE user_progress SET practiceScore = :score, totalAttempts = :attempts, successfulAttempts = :successful WHERE ruleId = :ruleId")
    suspend fun updateScore(ruleId: Int, score: Float, attempts: Int, successful: Int)

    @Query("SELECT * FROM user_progress")
    fun getOverallProgress(): Flow<List<UserProgress>>

    @Query("DELETE FROM user_progress")
    suspend fun deleteAll()
}
