package com.tajweed.ustoz.data.model

data class PracticeSession(
    val ruleId: Int,
    val ayahText: String,
    val expectedPronunciation: String,
    val attempts: Int = 0,
    val bestScore: Float = 0f
)
