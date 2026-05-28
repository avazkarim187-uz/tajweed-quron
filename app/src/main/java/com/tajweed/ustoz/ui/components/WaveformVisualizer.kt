package com.tajweed.ustoz.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaveformVisualizer(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    val barCount = 7
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val barHeights = List(barCount) { index ->
        val targetValue = if (isActive) 0.3f + (index % 3) * 0.25f else 0.15f
        val animValue by infiniteTransition.animateFloat(
            initialValue = if (isActive) 0.2f else 0.15f,
            targetValue = if (isActive) targetValue + 0.3f else 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400 + index * 80,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar_$index"
        )
        animValue
    }

    Canvas(
        modifier = modifier
            .width(120.dp)
            .height(48.dp)
    ) {
        val barWidth = size.width / (barCount * 2f)
        val spacing = barWidth

        barHeights.forEachIndexed { index, heightFraction ->
            val barHeight = size.height * heightFraction
            val x = index * (barWidth + spacing) + spacing / 2
            val y = (size.height - barHeight) / 2

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}
