package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getProgress(): Flow<List<UserProgress>>
    fun getProgressByRule(ruleId: Int): Flow<UserProgress?>
    suspend fun updateProgress(progress: UserProgress)
    fun getOverallStats(): Flow<List<UserProgress>>
}
