package com.example.phylax.ui.screens.emergency

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phylax.ui.theme.Background
import com.example.phylax.ui.theme.EmergencyRed
import com.example.phylax.ui.theme.PrimaryText
import kotlinx.coroutines.delay

@Composable
fun EmergencyActivationScreen(
    onCountdownFinished: () -> Unit,
    onCancel: () -> Unit
) {
    var countdown by remember { mutableIntStateOf(3) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        onCountdownFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sending emergency alert...",
            style = MaterialTheme.typography.titleLarge.copy(color = PrimaryText)
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        val infiniteTransition = rememberInfiniteTransition(label = "countdown")
        val animatedScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(animation = tween(500), repeatMode = RepeatMode.Reverse),
            label = "scale"
        )

        Text(
            text = countdown.toString(),
            modifier = Modifier.scale(animatedScale),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 120.sp,
                fontWeight = FontWeight.Black,
                color = EmergencyRed
            )
        )
        
        Spacer(modifier = Modifier.height(120.dp))
        
        TextButton(onClick = onCancel) {
            Text(
                text = "CANCEL",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = EmergencyRed,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
