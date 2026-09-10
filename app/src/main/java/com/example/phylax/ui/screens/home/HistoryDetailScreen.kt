package com.example.phylax.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.phylax.ui.theme.*

@Composable
fun HistoryDetailScreen(
    onBack: () -> Unit
) {
    // In a real app, we would pass the record ID and fetch it
    // For now, we'll use a sample Detail view
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PrimaryText)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alert Details",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = EmergencyRed.copy(alpha = 0.1f)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.padding(16.dp), tint = EmergencyRed)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Emergency Alert", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Today — 8:42 PM", style = MaterialTheme.typography.bodyMedium.copy(color = SecondaryText))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = BorderColor.copy(alpha = 0.5f))

                DetailItem(label = "Duration", value = "12 minutes")
                DetailItem(label = "Contacts Notified", value = "Mom, Dad, John")
                DetailItem(label = "Status", value = "Resolved", valueColor = SuccessGreen)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = LocationBlue)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("VIEW LAST LOCATION", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, valueColor: Color = PrimaryText) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium.copy(color = SecondaryText))
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(color = valueColor, fontWeight = FontWeight.SemiBold))
    }
}
