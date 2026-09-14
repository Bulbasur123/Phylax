package com.example.phylax.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phylax.ui.components.PhylaxShield
import com.example.phylax.ui.components.PremiumButton
import com.example.phylax.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.White, Background)
                )
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PhylaxShield(size = 140.dp)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Phylax",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp
                )
            )

            Text(
                text = "Your safety, one tap away.",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = SecondaryText,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Quickly alert your loved ones, share your location, and get attention when you need help.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SecondaryText,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            PremiumButton(
                text = "Get Started",
                onClick = onGetStarted
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
