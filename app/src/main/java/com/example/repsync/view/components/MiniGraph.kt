package com.example.repsync.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import com.example.repsync.ui.theme.graphColor

@Composable
fun MiniGraph(
    values: List<Float>,
    modifier: Modifier = Modifier
) {

    if (values.size < 2) {
        return
    }

    Canvas(
        modifier = modifier
    ) {

        val maxValue = values.maxOrNull() ?: 1f
        val minValue = values.minOrNull() ?: 0f

        val range = (maxValue - minValue)
            .takeIf { it > 0f }
            ?: 1f

        val stepX =
            size.width / (values.size - 1)

        val points = values.mapIndexed { index, value ->

            val x = index * stepX

            val normalized =
                (value - minValue) / range

            val y =
                size.height -
                        (normalized * size.height)

            Offset(x, y)
        }

        for (index in 0 until points.lastIndex) {

            drawLine(
                color = graphColor,
                start = points[index],
                end = points[index + 1],
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
        }

        points.forEach { point ->

            drawCircle(
                color = graphColor,
                radius = 3f,
                center = point
            )
        }
    }
}