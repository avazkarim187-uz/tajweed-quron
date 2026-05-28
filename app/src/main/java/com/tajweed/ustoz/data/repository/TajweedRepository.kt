package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.model.TajweedRule
import kotlinx.coroutines.flow.Flow

interface TajweedRepository {
    fun getAllRules(): Flow<List<TajweedRule>>
    fun getRuleById(id: Int): Flow<TajweedRule?>
    fun getRulesByCategory(category: String): Flow<List<TajweedRule>>
}
