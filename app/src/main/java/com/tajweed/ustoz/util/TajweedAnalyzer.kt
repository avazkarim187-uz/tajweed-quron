package com.tajweed.ustoz.util

class TajweedAnalyzer {

    fun analyzeTranscription(
        expected: String,
        actual: String,
        ruleIds: List<Int>
    ): AnalysisResult {
        val errors = mutableListOf<TajweedError>()
        val suggestions = mutableListOf<String>()

        val expectedChars = expected.trim()
        val actualChars = actual.trim()

        if (actualChars.isEmpty()) {
            return AnalysisResult(
                score = 0f,
                errors = listOf(
                    TajweedError(
                        errorType = "empty_transcription",
                        position = 0,
                        expected = expected,
                        actual = "",
                        ruleName = "Umumiy",
                        severity = "high"
                    )
                ),
                suggestions = listOf("Iltimos, aniqroq talaffuz qiling va qaytadan urinib ko'ring.")
            )
        }

        // Basic character-by-character comparison
        val minLength = minOf(expectedChars.length, actualChars.length)
        var matchCount = 0

        for (i in 0 until minLength) {
            if (expectedChars[i] == actualChars[i]) {
                matchCount++
            } else {
                errors.add(
                    TajweedError(
                        errorType = "mismatch",
                        position = i,
                        expected = expectedChars[i].toString(),
                        actual = actualChars[i].toString(),
                        ruleName = "Harf xatosi",
                        severity = if (i < expectedChars.length / 2) "high" else "medium"
                    )
                )
            }
        }

        // Handle length differences
        if (expectedChars.length != actualChars.length) {
            val diff = kotlin.math.abs(expectedChars.length - actualChars.length)
            if (expectedChars.length > actualChars.length) {
                suggestions.add("$diff ta harf tushib qolgan. To'liq oyatni o'qing.")
            } else {
                suggestions.add("$diff ta ortiqcha harf bor. Oyatni diqqat bilan o'qing.")
            }
        }

        // Calculate score based on match ratio
        val maxLength = maxOf(expectedChars.length, actualChars.length)
        val score = if (maxLength > 0) {
            (matchCount.toFloat() / maxLength.toFloat()) * 100f
        } else {
            0f
        }

        // Add general suggestions based on score
        if (score < Constants.MIN_PASSING_SCORE) {
            suggestions.add("Tajvid qoidalariga e'tibor bering va sekinroq o'qing.")
        } else if (score < 90f) {
            suggestions.add("Yaxshi! Bir nechta kichik xatolar bor, qayta mashq qiling.")
        }

        return AnalysisResult(
            score = score.coerceIn(0f, 100f),
            errors = errors,
            suggestions = suggestions
        )
    }
}

data class TajweedError(
    val errorType: String,
    val position: Int,
    val expected: String,
    val actual: String,
    val ruleName: String,
    val severity: String
)

data class AnalysisResult(
    val score: Float,
    val errors: List<TajweedError>,
    val suggestions: List<String>
)
