package com.tajweed.ustoz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ruleId: Int,
    val lessonCompleted: Boolean = false,
    val practiceScore: Float = 0f,
    val lastPracticeDate: Long = 0L,
    val totalAttempts: Int = 0,
    val successfulAttempts: Int = 0
)
