package com.example.phylax.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phylax.ui.components.SOSButton
import com.example.phylax.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToSOS: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAlarm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good evening 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Stay safe, always.",
                    style = MaterialTheme.typography.bodyLarge.copy(color = SecondaryText)
                )
            }
            
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp,
                onClick = onNavigateToSettings
            ) {
                // Avatar placeholder
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = SecondaryText)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Location Status Card
        StatusCard()
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // SOS Area
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SOSButton(
                onActivated = onNavigateToSOS,
                onProgress = {}
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "PRESS & HOLD",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = SecondaryText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(
                color = PrimaryText,
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionCard(
                title = "Alarm", 
                subtitle = "Loud Alarm", 
                icon = Icons.Default.Notifications, 
                color = EmergencyRed, 
                modifier = Modifier.weight(1f),
                onClick = onNavigateToAlarm
            )
            ActionCard(
                title = "Location", 
                subtitle = "Share", 
                icon = Icons.Default.Place, 
                color = LocationBlue, 
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToHistory() } // For now, navigate to history or a map view
            )
            ActionCard(
                title = "Contacts", 
                subtitle = "Emergency", 
                icon = Icons.Default.Group, 
                color = VioletAccent, 
                modifier = Modifier.weight(1f),
                onClick = onNavigateToContacts
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Safety Status Card
        SafetyCheckCard()
    }
}

@Composable
fun StatusCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "status")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.4f,
                animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse),
                label = "alpha"
            )
            
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = alpha))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "Location Sharing ON",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String, 
    subtitle: String, 
    icon: ImageVector, 
    color: Color, 
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.height(140.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(8.dp), tint = color)
            }
            
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText))
            }
        }
    }
}

@Composable
fun SafetyCheckCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "You're protected",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SafetyCheckItem("Emergency contacts ready")
            SafetyCheckItem("Location permission enabled")
            SafetyCheckItem("SOS protection active")
        }
    }
}

@Composable
fun SafetyCheckItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Check,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium.copy(color = SecondaryText))
    }
}
