package com.tajweed.ustoz.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TajweedAnalyzer @Inject constructor() {

    // Rule ID to rule name mapping for rule-specific error messages
    private val ruleNames = mapOf(
        1 to "Izhori halqiy",
        2 to "Idg'om bila g'unna",
        3 to "Idg'om ma'al g'unna",
        4 to "Iqlob",
        5 to "Ikhfo",
        6 to "Idg'omi mitslain",
        7 to "Madd tabiiy",
        8 to "Madd muttasil",
        9 to "Madd munfasil",
        10 to "Qalqala",
        11 to "Lom ta'rif",
        12 to "Tafkhim va tarqiq"
    )

    // Rule ID to relevant letters for detecting rule-specific errors
    private val ruleLetters = mapOf(
        1 to setOf('\u0621', '\u0647', '\u0639', '\u062D', '\u063A', '\u062E'), // ء ه ع ح غ خ
        2 to setOf('\u0644', '\u0631'), // ل ر
        3 to setOf('\u064A', '\u0646', '\u0645', '\u0648'), // ي ن م و
        4 to setOf('\u0628'), // ب
        5 to setOf('\u062A', '\u062B', '\u062C', '\u062F', '\u0630', '\u0632', '\u0633', '\u0634', '\u0635', '\u0636', '\u0637', '\u0638', '\u0641', '\u0642', '\u0643'), // ikhfa letters
        10 to setOf('\u0642', '\u0637', '\u0628', '\u062C', '\u062F') // qalqala letters ق ط ب ج د
    )

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

        // Use Levenshtein-style edit distance alignment for better comparison
        val editOps = computeEditOperations(expectedChars, actualChars)

        var matchCount = 0
        var totalOperations = 0

        for (op in editOps) {
            totalOperations++
            when (op) {
                is EditOp.Match -> {
                    matchCount++
                }
                is EditOp.Substitute -> {
                    val ruleName = determineRuleName(op.expectedChar, op.actualChar, ruleIds)
                    val severity = determineSeverity(op.expectedChar, op.position, expectedChars.length)
                    errors.add(
                        TajweedError(
                            errorType = "substitution",
                            position = op.position,
                            expected = op.expectedChar.toString(),
                            actual = op.actualChar.toString(),
                            ruleName = ruleName,
                            severity = severity
                        )
                    )
                }
                is EditOp.Insert -> {
                    errors.add(
                        TajweedError(
                            errorType = "insertion",
                            position = op.position,
                            expected = "",
                            actual = op.actualChar.toString(),
                            ruleName = determineInsertionRuleName(op.actualChar, ruleIds),
                            severity = "medium"
                        )
                    )
                }
                is EditOp.Delete -> {
                    errors.add(
                        TajweedError(
                            errorType = "deletion",
                            position = op.position,
                            expected = op.expectedChar.toString(),
                            actual = "",
                            ruleName = determineDeletionRuleName(op.expectedChar, ruleIds),
                            severity = "high"
                        )
                    )
                }
            }
        }

        // Calculate score based on matches vs total aligned length
        val maxLength = maxOf(expectedChars.length, actualChars.length)
        val score = if (maxLength > 0) {
            (matchCount.toFloat() / maxLength.toFloat()) * 100f
        } else {
            0f
        }

        // Add rule-specific suggestions
        if (ruleIds.isNotEmpty() && errors.isNotEmpty()) {
            for (ruleId in ruleIds) {
                val ruleName = ruleNames[ruleId]
                if (ruleName != null) {
                    val ruleErrors = errors.count { it.ruleName == ruleName }
                    if (ruleErrors > 0) {
                        suggestions.add("\"$ruleName\" qoidasida $ruleErrors ta xato topildi. Ushbu qoidani qayta o'rganing.")
                    }
                }
            }
        }

        // Handle length differences
        val insertions = editOps.count { it is EditOp.Insert }
        val deletions = editOps.count { it is EditOp.Delete }
        if (deletions > 0) {
            suggestions.add("$deletions ta harf tushib qolgan. To'liq oyatni o'qing.")
        }
        if (insertions > 0) {
            suggestions.add("$insertions ta ortiqcha harf bor. Oyatni diqqat bilan o'qing.")
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

    /**
     * Computes the edit operations (match, substitute, insert, delete) to transform
     * expected into actual using a Levenshtein-style dynamic programming approach.
     */
    private fun computeEditOperations(expected: String, actual: String): List<EditOp> {
        val m = expected.length
        val n = actual.length

        // dp[i][j] = minimum edit distance for expected[0..i-1] and actual[0..j-1]
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (expected[i - 1] == actual[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution or match
                )
            }
        }

        // Backtrack to find the actual operations
        val ops = mutableListOf<EditOp>()
        var i = m
        var j = n

        while (i > 0 || j > 0) {
            when {
                i > 0 && j > 0 && expected[i - 1] == actual[j - 1] -> {
                    ops.add(EditOp.Match(position = i - 1, char = expected[i - 1]))
                    i--
                    j--
                }
                i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1 -> {
                    ops.add(EditOp.Substitute(position = i - 1, expectedChar = expected[i - 1], actualChar = actual[j - 1]))
                    i--
                    j--
                }
                j > 0 && dp[i][j] == dp[i][j - 1] + 1 -> {
                    ops.add(EditOp.Insert(position = i, actualChar = actual[j - 1]))
                    j--
                }
                i > 0 && dp[i][j] == dp[i - 1][j] + 1 -> {
                    ops.add(EditOp.Delete(position = i - 1, expectedChar = expected[i - 1]))
                    i--
                }
                else -> {
                    // Fallback: should not happen, but handle gracefully
                    if (i > 0) {
                        ops.add(EditOp.Delete(position = i - 1, expectedChar = expected[i - 1]))
                        i--
                    } else if (j > 0) {
                        ops.add(EditOp.Insert(position = i, actualChar = actual[j - 1]))
                        j--
                    }
                }
            }
        }

        return ops.reversed()
    }

    private fun determineRuleName(expectedChar: Char, actualChar: Char, ruleIds: List<Int>): String {
        for (ruleId in ruleIds) {
            val letters = ruleLetters[ruleId]
            if (letters != null && (expectedChar in letters || actualChar in letters)) {
                return ruleNames[ruleId] ?: "Harf xatosi"
            }
        }
        // Check if it is a haraka (diacritical mark) error
        if (isHaraka(expectedChar) || isHaraka(actualChar)) {
            return "Harakat xatosi"
        }
        return "Harf xatosi"
    }

    private fun determineInsertionRuleName(actualChar: Char, ruleIds: List<Int>): String {
        for (ruleId in ruleIds) {
            val letters = ruleLetters[ruleId]
            if (letters != null && actualChar in letters) {
                return ruleNames[ruleId] ?: "Ortiqcha harf"
            }
        }
        return "Ortiqcha harf"
    }

    private fun determineDeletionRuleName(expectedChar: Char, ruleIds: List<Int>): String {
        for (ruleId in ruleIds) {
            val letters = ruleLetters[ruleId]
            if (letters != null && expectedChar in letters) {
                return ruleNames[ruleId] ?: "Tushib qolgan harf"
            }
        }
        return "Tushib qolgan harf"
    }

    private fun determineSeverity(expectedChar: Char, position: Int, totalLength: Int): String {
        // Errors in the first half are more severe; haraka errors are medium
        return when {
            isHaraka(expectedChar) -> "low"
            position < totalLength / 3 -> "high"
            position < totalLength * 2 / 3 -> "medium"
            else -> "low"
        }
    }

    private fun isHaraka(char: Char): Boolean {
        // Arabic diacritical marks (harakat) range
        return char.code in 0x064B..0x065F || char.code == 0x0670
    }
}

sealed class EditOp {
    data class Match(val position: Int, val char: Char) : EditOp()
    data class Substitute(val position: Int, val expectedChar: Char, val actualChar: Char) : EditOp()
    data class Insert(val position: Int, val actualChar: Char) : EditOp()
    data class Delete(val position: Int, val expectedChar: Char) : EditOp()
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
