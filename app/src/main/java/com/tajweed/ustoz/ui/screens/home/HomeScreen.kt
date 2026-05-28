package com.tajweed.ustoz.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tajweed.ustoz.ui.components.TajweedProgressIndicator

@Composable
fun HomeScreen(
    onNavigateToLessons: () -> Unit,
    onNavigateToQuran: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToLesson: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeHeader()
        }

        item {
            DailyStatsCard(
                totalRules = uiState.totalRules,
                completedRules = uiState.completedRules,
                overallScore = uiState.overallScore,
                streak = uiState.streak
            )
        }

        if (uiState.lastLesson != null) {
            item {
                ContinueButton(
                    lessonName = uiState.lastLesson!!.nameUz,
                    onClick = { onNavigateToLesson(uiState.lastLesson!!.id) }
                )
            }
        }

        item {
            QuickActionsRow(
                onLessonsClick = onNavigateToLessons,
                onQuranClick = onNavigateToQuran,
                onPracticeClick = onNavigateToPractice
            )
        }

        if (uiState.recentResults.isNotEmpty()) {
            item {
                Text(
                    text = "Oxirgi natijalar",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(uiState.recentResults.size) { index ->
                val result = uiState.recentResults[index]
                RecentResultItem(result = result)
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Column {
        Text(
            text = "Assalomu alaykum!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tajvid o'rganishda davom eting",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DailyStatsCard(
    totalRules: Int,
    completedRules: Int,
    overallScore: Float,
    streak: Int
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TajweedProgressIndicator(
                progress = if (totalRules > 0) completedRules.toFloat() / totalRules else 0f,
                label = "Darslar",
                size = 64.dp
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$streak",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "kun ketma-ket",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${overallScore.toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "umumiy ball",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ContinueButton(
    lessonName: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Davom etish: $lessonName")
    }
}

@Composable
private fun QuickActionsRow(
    onLessonsClick: () -> Unit,
    onQuranClick: () -> Unit,
    onPracticeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard(
            title = "Darslar",
            icon = Icons.Default.List,
            onClick = onLessonsClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Qur'on",
            icon = Icons.Default.MenuBook,
            onClick = onQuranClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Mashq",
            icon = Icons.Default.Mic,
            onClick = onPracticeClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecentResultItem(result: com.tajweed.ustoz.data.model.UserProgress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Qoida #${result.ruleId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${result.practiceScore.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
