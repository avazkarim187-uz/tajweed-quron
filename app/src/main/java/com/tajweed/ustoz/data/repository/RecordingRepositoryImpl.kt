package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.local.RecordingResultDao
import com.tajweed.ustoz.data.model.RecordingResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordingRepositoryImpl @Inject constructor(
    private val recordingResultDao: RecordingResultDao
) : RecordingRepository {

    override suspend fun saveResult(result: RecordingResult): Long {
        return recordingResultDao.insert(result)
    }

    override fun getResults(): Flow<List<RecordingResult>> {
        return recordingResultDao.getAll()
    }

    override fun getResultById(id: Int): Flow<RecordingResult?> {
        return recordingResultDao.getById(id)
    }

    override fun getRecentResults(limit: Int): Flow<List<RecordingResult>> {
        return recordingResultDao.getRecentResults(limit)
    }
}
