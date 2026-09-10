package com.example.phylax.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.phylax.ui.theme.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToSafetySetup: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PrimaryText)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SettingsSection(title = "Account") {
            SettingsItem(title = "Profile Information", icon = Icons.Default.Person, onClick = onNavigateToProfile)
            SettingsItem(title = "Phone Number", icon = Icons.Default.Phone, onClick = onNavigateToProfile)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection(title = "Safety") {
            SettingsItem(title = "Emergency Contacts", icon = Icons.Default.Group, onClick = onNavigateToContacts)
            SettingsItem(title = "SOS Settings", icon = Icons.Default.Sos, onClick = onNavigateToSafetySetup)
            SettingsItem(title = "Permissions", icon = Icons.Default.Lock, onClick = onNavigateToPermissions)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection(title = "Other") {
            SettingsItem(title = "About Phylax", icon = Icons.Default.Info, onClick = {})
            SettingsItem(title = "Log Out", icon = Icons.AutoMirrored.Filled.Logout, tint = EmergencyRed, onClick = { showLogoutDialog = true })
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out of Phylax? You will need to log in again to access your dashboard.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("LOG OUT", color = EmergencyRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("CANCEL", color = SecondaryText)
                }
            },
            shape = MaterialTheme.shapes.medium,
            containerColor = Color.White
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(color = SecondaryText, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, onClick: () -> Unit, tint: Color = PrimaryText) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(color = tint))
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SecondaryText)
    }
}
