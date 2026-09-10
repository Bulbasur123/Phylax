package com.example.phylax.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phylax.ui.theme.EmergencyRed
import kotlinx.coroutines.delay

@Composable
fun SOSButton(
    onActivated: () -> Unit,
    onProgress: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val view = LocalView.current

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(isPressing) {
        if (isPressing) {
            val startTime = System.currentTimeMillis()
            while (isPressing && progress < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                progress = (elapsed / 3000f).coerceIn(0f, 1f)
                onProgress(progress)
                if (progress >= 1f) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    onActivated()
                    isPressing = false
                }
                delay(16)
            }
        } else {
            progress = 0f
            onProgress(0f)
        }
    }

    Box(
        modifier = modifier
            .size(240.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressing = true
                        tryAwaitRelease()
                        isPressing = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Pulse Ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(if (isPressing) 1f else pulseScale)
                .background(EmergencyRed.copy(alpha = 0.1f), CircleShape)
        )

        // Outer Ring / Progress
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                style = Stroke(width = 8.dp.toPx())
            )
            drawArc(
                color = EmergencyRed,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
        }

        // Main Button
        Surface(
            modifier = Modifier
                .size(180.dp)
                .scale(if (isPressing) 0.95f else 1f),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "SOS",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = EmergencyRed,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                )
                
                if (isPressing) {
                    Text(
                        text = "HOLD",
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = EmergencyRed.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
