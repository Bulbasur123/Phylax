package com.example.phylax.ui.screens.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.phylax.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun LiveLocationScreen(
    onBack: () -> Unit
) {
    val userLocation = LatLng(14.5995, 120.9842) // Placeholder for Manila
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            val markerState = rememberMarkerState(position = userLocation)
            Marker(
                state = markerState,
                title = "Your Location",
                snippet = "Emergency Active"
            )
        }
        
        // Floating Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .statusBarsPadding()
                .size(48.dp)
                .background(Color.White, MaterialTheme.shapes.medium)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = PrimaryText)
        }
        
        // Status Card
        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
                    color = SuccessGreen.copy(alpha = 0.1f)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.padding(8.dp), tint = SuccessGreen)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(text = "LIVE LOCATION", style = MaterialTheme.typography.labelLarge.copy(color = SuccessGreen, fontWeight = FontWeight.Black))
                    Text(text = "Updated 5 seconds ago", style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText))
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                TextButton(onClick = {}) {
                    Text("OPEN IN MAPS", color = LocationBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
