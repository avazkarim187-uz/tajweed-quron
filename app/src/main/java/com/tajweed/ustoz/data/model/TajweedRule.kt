package com.tajweed.ustoz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tajweed_rules")
data class TajweedRule(
    @PrimaryKey
    val id: Int,
    val name: String,
    val nameUz: String,
    val description: String,
    val arabicExample: String,
    val explanation: String,
    val audioUrl: String? = null,
    val category: String,
    val lettersList: String
)
