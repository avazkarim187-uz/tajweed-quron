package com.tajweed.ustoz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.tajweed.ustoz.ui.theme.TajweedGhunna
import com.tajweed.ustoz.ui.theme.TajweedIdghom
import com.tajweed.ustoz.ui.theme.TajweedIkhfo
import com.tajweed.ustoz.ui.theme.TajweedIqlab
import com.tajweed.ustoz.ui.theme.TajweedIzhor
import com.tajweed.ustoz.ui.theme.TajweedMadd
import com.tajweed.ustoz.ui.theme.TajweedQalqala

/**
 * Tajweed color-coding map:
 * Green = Izhor
 * Blue = Idg'om
 * Orange = Ikhfo
 * Purple = Iqlab
 * Red = Ghunna
 * Brown = Qalqala
 * Teal = Madd
 */
@Composable
fun ArabicText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign = TextAlign.Right,
    tajweedHighlights: Map<IntRange, Color> = emptyMap()
) {
    if (tajweedHighlights.isEmpty()) {
        Text(
            text = text,
            modifier = modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = fontSize,
                textDirection = TextDirection.Rtl
            ),
            color = color,
            textAlign = textAlign
        )
    } else {
        val annotatedString = buildAnnotatedString {
            withStyle(SpanStyle(color = color)) {
                append(text)
            }
            tajweedHighlights.forEach { (range, highlightColor) ->
                val start = range.first.coerceIn(0, text.length)
                val end = range.last.coerceIn(0, text.length)
                if (start < end) {
                    addStyle(
                        SpanStyle(color = highlightColor),
                        start,
                        end
                    )
                }
            }
        }
        Text(
            text = annotatedString,
            modifier = modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = fontSize,
                textDirection = TextDirection.Rtl
            ),
            textAlign = textAlign
        )
    }
}

/**
 * Returns the tajweed color for a given rule category name.
 */
fun getTajweedColor(category: String): Color {
    return when {
        category.contains("Izhor", ignoreCase = true) -> TajweedIzhor
        category.contains("Idg'om", ignoreCase = true) ||
            category.contains("Idghom", ignoreCase = true) -> TajweedIdghom
        category.contains("Ikhfo", ignoreCase = true) -> TajweedIkhfo
        category.contains("Iqlab", ignoreCase = true) -> TajweedIqlab
        category.contains("Ghunna", ignoreCase = true) -> TajweedGhunna
        category.contains("Qalqala", ignoreCase = true) -> TajweedQalqala
        category.contains("Madd", ignoreCase = true) -> TajweedMadd
        else -> TajweedIzhor
    }
}
