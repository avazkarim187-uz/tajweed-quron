package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.local.UserProgressDao
import com.tajweed.ustoz.data.model.UserProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val userProgressDao: UserProgressDao
) : ProgressRepository {

    override fun getProgress(): Flow<List<UserProgress>> {
        return userProgressDao.getAllProgress()
    }

    override fun getProgressByRule(ruleId: Int): Flow<UserProgress?> {
        return userProgressDao.getProgressByRule(ruleId)
    }

    override suspend fun updateProgress(progress: UserProgress) {
        userProgressDao.insertOrUpdate(progress)
    }

    override fun getOverallStats(): Flow<List<UserProgress>> {
        return userProgressDao.getOverallProgress()
    }
}
