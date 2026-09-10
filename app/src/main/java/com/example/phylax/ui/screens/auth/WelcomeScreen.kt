package com.example.phylax.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phylax.ui.components.PhylaxShield
import com.example.phylax.ui.components.PremiumButton
import com.example.phylax.ui.theme.Background
import com.example.phylax.ui.theme.PrimaryText
import com.example.phylax.ui.theme.SecondaryText

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PhylaxShield(size = 180.dp)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Phylax",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = PrimaryText,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
        )
        
        Text(
            text = "Your safety, one tap away.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = SecondaryText,
                fontWeight = FontWeight.Medium
            )
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Quickly alert your loved ones, share your location, and get attention when you need help.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = SecondaryText,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        PremiumButton(
            text = "GET STARTED",
            onClick = onGetStarted
        )
    }
}
