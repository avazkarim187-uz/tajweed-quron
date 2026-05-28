package com.tajweed.ustoz.data.repository

import com.tajweed.ustoz.data.local.TajweedRuleDao
import com.tajweed.ustoz.data.model.TajweedRule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TajweedRepositoryImpl @Inject constructor(
    private val tajweedRuleDao: TajweedRuleDao
) : TajweedRepository {

    override fun getAllRules(): Flow<List<TajweedRule>> {
        return tajweedRuleDao.getAllRules()
    }

    override fun getRuleById(id: Int): Flow<TajweedRule?> {
        return tajweedRuleDao.getRuleById(id)
    }

    override fun getRulesByCategory(category: String): Flow<List<TajweedRule>> {
        return tajweedRuleDao.getRulesByCategory(category)
    }
}
