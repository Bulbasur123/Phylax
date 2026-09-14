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
    onNavigateToAlarm: () -> Unit,
    onNavigateToLocation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        
        // iOS 18 Style Large Title
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Phylax",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = PrimaryText,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(
                    text = "Your safety dashboard",
                    style = MaterialTheme.typography.bodyLarge.copy(color = SecondaryText)
                )
            }
            
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color.White,
                onClick = onNavigateToSettings,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = SecondaryText)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // Location Status (iOS Style Inset Card)
        StatusCard()
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // SOS Button Center
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SOSButton(
                onActivated = onNavigateToSOS
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // iOS 18 Style Control Center Modules
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionCard(
                title = "Alarm", 
                subtitle = "Siren", 
                icon = Icons.Default.Notifications, 
                color = iOSRed, 
                modifier = Modifier.weight(1f),
                onClick = onNavigateToAlarm
            )
            ActionCard(
                title = "Live", 
                subtitle = "Location", 
                icon = Icons.Default.Place, 
                color = iOSBlue, 
                modifier = Modifier.weight(1f),
                onClick = onNavigateToLocation
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ActionCardLarge(
            title = "Emergency Contacts",
            subtitle = "Manage your guardians",
            icon = Icons.Default.Group,
            color = iOSIndigo,
            onClick = onNavigateToContacts
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Bottom Utility Bar (iOS style)
        SafetyCheckCard()
        
        Spacer(modifier = Modifier.height(40.dp).navigationBarsPadding())
    }
}

@Composable
fun StatusCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(iOSGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "System Protected",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "LIVE",
                style = MaterialTheme.typography.labelMedium.copy(color = iOSGreen, fontWeight = FontWeight.Bold)
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
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = color)
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                Text(text = subtitle, style = MaterialTheme.typography.labelMedium.copy(color = SecondaryText))
            }
        }
    }
}

@Composable
fun ActionCardLarge(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(12.dp), tint = color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
                Text(text = subtitle, style = MaterialTheme.typography.labelMedium.copy(color = SecondaryText))
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun SafetyCheckCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "System Protection",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
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
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = iOSGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium.copy(color = SecondaryText))
    }
}
