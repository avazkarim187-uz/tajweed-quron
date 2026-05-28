package com.tajweed.ustoz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tajweed.ustoz.data.model.TajweedRule
import kotlinx.coroutines.flow.Flow

@Dao
interface TajweedRuleDao {

    @Query("SELECT * FROM tajweed_rules")
    fun getAllRules(): Flow<List<TajweedRule>>

    @Query("SELECT * FROM tajweed_rules WHERE id = :id")
    fun getRuleById(id: Int): Flow<TajweedRule?>

    @Query("SELECT * FROM tajweed_rules WHERE category = :category")
    fun getRulesByCategory(category: String): Flow<List<TajweedRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules: List<TajweedRule>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: TajweedRule)
}
