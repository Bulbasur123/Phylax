package com.example.phylax.ui.screens.emergency

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.phylax.ui.components.PremiumButton
import com.example.phylax.ui.theme.*
import com.example.phylax.service.SOSService
import kotlinx.coroutines.delay

@Composable
fun EmergencyDashboard(
    userId: String,
    onStopSOS: () -> Unit,
    onViewMap: () -> Unit
) {
    val context = LocalContext.current
    var timerSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Start SOS Service
        val intent = Intent(context, SOSService::class.java).apply {
            action = SOSService.ACTION_START_SOS
            putExtra(SOSService.EXTRA_USER_ID, userId)
        }
        ContextCompat.startForegroundService(context, intent)

        while (true) {
            delay(1000)
            timerSeconds++
        }
    }

    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60
    val timeString = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ALERT SENT",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = EmergencyRed,
                    fontWeight = FontWeight.Black
                )
            )
            Text(
                text = "Your emergency contacts have been notified.",
                style = MaterialTheme.typography.bodyLarge.copy(color = SecondaryText)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        EmergencyStatusCard(
            title = "Live Location",
            status = "SHARING",
            icon = Icons.Default.ShareLocation,
            color = SuccessGreen
        )
        
        EmergencyStatusCard(
            title = "Loud Alarm",
            status = "ON",
            icon = Icons.Default.Notifications,
            color = EmergencyRed
        )
        
        EmergencyStatusCard(
            title = "Emergency Timer",
            status = timeString,
            icon = Icons.Default.Schedule,
            color = PrimaryText
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        PremiumButton(
            text = "STOP ALARM",
            onClick = {},
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        PremiumButton(
            text = "VIEW LIVE LOCATION",
            onClick = onViewMap,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        PremiumButton(
            text = "STOP SOS SESSION",
            onClick = {
                // Stop SOS Service
                val intent = Intent(context, SOSService::class.java).apply {
                    action = SOSService.ACTION_STOP_SOS
                }
                context.startService(intent)
                onStopSOS()
            },
            isEmergency = true
        )
        
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = EmergencyRed),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Phone, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("CALL EMERGENCY SERVICES", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyStatusCard(title: String, status: String, icon: ImageVector, color: Color) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small,
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(8.dp), tint = color)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(color = PrimaryText, fontWeight = FontWeight.SemiBold))
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(text = status, style = MaterialTheme.typography.titleMedium.copy(color = color, fontWeight = FontWeight.Black))
        }
    }
}
