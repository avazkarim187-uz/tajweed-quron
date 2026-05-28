package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.model.RecordingResult
import kotlinx.coroutines.flow.Flow

interface RecordingRepository {
    suspend fun saveResult(result: RecordingResult)
    fun getResults(): Flow<List<RecordingResult>>
    fun getRecentResults(limit: Int): Flow<List<RecordingResult>>
}
