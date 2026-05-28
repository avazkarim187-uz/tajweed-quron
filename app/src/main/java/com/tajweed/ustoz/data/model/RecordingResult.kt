package com.tajweed.ustoz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recording_results")
data class RecordingResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ruleId: Int? = null,
    val ayahId: Int? = null,
    val audioPath: String,
    val transcription: String,
    val score: Float,
    val errors: String,
    val timestamp: Long
)
