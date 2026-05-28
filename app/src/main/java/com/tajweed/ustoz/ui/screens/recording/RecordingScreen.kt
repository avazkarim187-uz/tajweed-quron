package com.tajweed.ustoz.ui.screens.recording

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tajweed.ustoz.navigation.Screen
import com.tajweed.ustoz.ui.components.ArabicText
import com.tajweed.ustoz.ui.components.PermissionHandler
import com.tajweed.ustoz.ui.components.RecordingButton
import com.tajweed.ustoz.ui.components.WaveformVisualizer

@Composable
fun RecordingScreen(
    ayahId: Int? = null,
    ruleId: Int? = null,
    navController: NavController,
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(ayahId, ruleId) {
        viewModel.loadTarget(ayahId, ruleId)
    }

    LaunchedEffect(uiState.state) {
        if (uiState.state == RecordingState.Done && uiState.resultId != null) {
            navController.navigate(Screen.Feedback.createRoute(uiState.resultId!!))
            viewModel.resetState()
        }
    }

    PermissionHandler(
        onPermissionGranted = {
            RecordingContent(
                uiState = uiState,
                onStartRecording = { viewModel.startRecording() },
                onStopRecording = { viewModel.stopRecording() },
                onCancel = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        },
        onCancel = { navController.popBackStack() }
    )
}

@Composable
private fun RecordingContent(
    uiState: RecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Instructions and target text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Quyidagi matnni o'qing",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Target Arabic text
            if (uiState.targetText.isNotEmpty()) {
                ArabicText(
                    text = uiState.targetText,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Transliteration
                Text(
                    text = uiState.targetTransliteration,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Middle section: Recording controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.state) {
                RecordingState.Processing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tahlil qilinmoqda...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                RecordingState.Error -> {
                    Text(
                        text = uiState.error ?: "Xatolik yuz berdi",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RecordingButton(
                        isRecording = false,
                        onClick = onStartRecording
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Qayta boshlash",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                else -> {
                    // Waveform visualizer
                    WaveformVisualizer(
                        isActive = uiState.state == RecordingState.Recording
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer
                    if (uiState.state == RecordingState.Recording) {
                        val minutes = uiState.recordingDuration / 60
                        val seconds = uiState.recordingDuration % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Record/Stop button
                    RecordingButton(
                        isRecording = uiState.state == RecordingState.Recording,
                        onClick = {
                            if (uiState.state == RecordingState.Recording) {
                                onStopRecording()
                            } else {
                                onStartRecording()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (uiState.state == RecordingState.Recording)
                            "To'xtatish" else "Boshlash",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (uiState.state == RecordingState.Recording)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Bottom section: Cancel button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = onCancel) {
                Text(text = "Bekor qilish")
            }
        }
    }
}
