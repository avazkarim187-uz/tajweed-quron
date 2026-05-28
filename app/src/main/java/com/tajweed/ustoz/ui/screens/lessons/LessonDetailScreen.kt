package com.tajweed.ustoz.ui.screens.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tajweed.ustoz.ui.components.ArabicText
import com.tajweed.ustoz.ui.components.RuleChip
import com.tajweed.ustoz.ui.components.getTajweedColor

@Composable
fun LessonDetailScreen(
    ruleId: Int,
    onBackClick: () -> Unit,
    onPracticeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LessonDetailViewModel = hiltViewModel()
) {
    val rule by viewModel.rule.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rule?.let { tajweedRule ->
            // Rule name in Arabic (large)
            ArabicText(
                text = tajweedRule.name,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )

            // Uzbek name
            Text(
                text = tajweedRule.nameUz,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            RuleChip(category = tajweedRule.category)

            // Ta'rif (Definition) section
            SectionCard(
                title = "Ta'rif",
                content = tajweedRule.description
            )

            // Qoida (Rule) section
            SectionCard(
                title = "Qoida",
                content = tajweedRule.explanation
            )

            // Harflar (Letters) section
            if (tajweedRule.lettersList.isNotBlank()) {
                SectionCard(
                    title = "Harflar",
                    content = tajweedRule.lettersList
                )
            }

            // Misollar (Examples) section
            Text(
                text = "Misollar",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val examples = tajweedRule.arabicExample.split("|")
            examples.forEach { example ->
                ExampleCard(
                    arabicText = example.trim(),
                    ruleColor = getTajweedColor(tajweedRule.category)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Practice button
            Button(
                onClick = { onPracticeClick(tajweedRule.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Mashq qilish")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExampleCard(
    arabicText: String,
    ruleColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            ArabicText(
                text = arabicText,
                fontSize = 26.sp,
                color = ruleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = { /* Audio playback */ }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Tinglash",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
