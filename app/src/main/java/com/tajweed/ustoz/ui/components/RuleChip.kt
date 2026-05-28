package com.tajweed.ustoz.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tajweed.ustoz.ui.theme.TajweedGhunna
import com.tajweed.ustoz.ui.theme.TajweedIdghom
import com.tajweed.ustoz.ui.theme.TajweedIkhfo
import com.tajweed.ustoz.ui.theme.TajweedIqlab
import com.tajweed.ustoz.ui.theme.TajweedIzhor
import com.tajweed.ustoz.ui.theme.TajweedMadd
import com.tajweed.ustoz.ui.theme.TajweedQalqala

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuleChip(
    category: String,
    modifier: Modifier = Modifier
) {
    val chipColor = when {
        category.contains("Nun Sakin", ignoreCase = true) -> TajweedIzhor
        category.contains("Meem Sakin", ignoreCase = true) -> TajweedIkhfo
        category.contains("Madd", ignoreCase = true) -> TajweedMadd
        category.contains("Qalqala", ignoreCase = true) -> TajweedQalqala
        category.contains("Ghunna", ignoreCase = true) -> TajweedGhunna
        category.contains("Idg'om", ignoreCase = true) -> TajweedIdghom
        category.contains("Iqlab", ignoreCase = true) -> TajweedIqlab
        else -> MaterialTheme.colorScheme.primary
    }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = chipColor.copy(alpha = 0.12f),
            labelColor = chipColor
        )
    )
}
