package com.example.repsync.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun ActivityRing(
    progress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 500
        ),
        label = "activity_progress"
    )

    val backgroundColor =
        MaterialTheme.colorScheme.surfaceVariant

    val progressColor =
        MaterialTheme.colorScheme.secondary

    val innerColor =
        MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val strokeWidth = 20.dp.toPx()

            val diameter = min(
                size.width,
                size.height
            )

            val radius = diameter / 2f

            val center = Offset(
                x = size.width / 2f,
                y = size.height / 2f
            )

            val ringDiameter =
                diameter - strokeWidth

            val ringTopLeft = Offset(
                x = center.x - ringDiameter / 2f,
                y = center.y - ringDiameter / 2f
            )

            drawCircle(
                color = innerColor,
                radius = radius - strokeWidth - 8.dp.toPx(),
                center = center
            )

            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = ringTopLeft,
                size = Size(
                    width = ringDiameter,
                    height = ringDiameter
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            if (animatedProgress > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = ringTopLeft,
                    size = Size(
                        width = ringDiameter,
                        height = ringDiameter
                    ),
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
        content()
    }
}