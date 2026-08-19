package com.example.repsync.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.example.repsync.ui.theme.graphColor
import kotlin.math.max

@Composable
fun StatGraph(
    values: List<Float>,
    modifier: Modifier = Modifier,
) {
    if (values.isEmpty()) {
        return
    }

    val maxValue = max(
        values.maxOrNull() ?: 0f,
        1f
    )

    val animatedValues = values.map { value ->

        val animatedValue by animateFloatAsState(
            targetValue = value,
            animationSpec = tween(500),
            label = "graphBar"
        )

        animatedValue
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {

        val barWidth = size.width / animatedValues.size
        val spacing = barWidth * 0.35f
        val actualBarWidth = barWidth - spacing

        animatedValues.forEachIndexed { index, value ->

            val barHeight =
                (value / maxValue) * size.height

            val left =
                index * barWidth + spacing / 2f

            val top =
                size.height - barHeight

            drawRoundRect(
                color = graphColor,
                topLeft = Offset(
                    x = left,
                    y = top
                ),
                size = Size(
                    width = actualBarWidth,
                    height = barHeight
                ),
                cornerRadius = CornerRadius(
                    x = actualBarWidth / 2f,
                    y = actualBarWidth / 2f
                )
            )
        }
    }
}