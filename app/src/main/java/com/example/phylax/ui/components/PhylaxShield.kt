package com.example.phylax.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.phylax.ui.theme.EmergencyRed
import com.example.phylax.ui.theme.LocationBlue
import com.example.phylax.ui.theme.VioletAccent

@Composable
fun PhylaxShield(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val width = size.toPx()
            val height = size.toPx()

            val shieldPath = Path().apply {
                moveTo(width * 0.5f, height * 0.05f)
                cubicTo(width * 0.8f, height * 0.05f, width * 0.95f, height * 0.2f, width * 0.95f, height * 0.45f)
                cubicTo(width * 0.95f, height * 0.7f, width * 0.7f, height * 0.95f, width * 0.5f, height * 0.95f)
                cubicTo(width * 0.3f, height * 0.95f, width * 0.05f, height * 0.7f, width * 0.05f, height * 0.45f)
                cubicTo(width * 0.05f, height * 0.2f, width * 0.2f, height * 0.05f, width * 0.5f, height * 0.05f)
                close()
            }

            drawPath(
                path = shieldPath,
                brush = Brush.linearGradient(
                    colors = listOf(LocationBlue, VioletAccent)
                ),
                style = Fill
            )

            // Inner Guardian detail
            val innerPath = Path().apply {
                moveTo(width * 0.5f, height * 0.25f)
                lineTo(width * 0.65f, height * 0.4f)
                lineTo(width * 0.5f, height * 0.75f)
                lineTo(width * 0.35f, height * 0.4f)
                close()
            }

            drawPath(
                path = innerPath,
                color = Color.White.copy(alpha = 0.8f),
                style = Fill
            )
        }
    }
}
