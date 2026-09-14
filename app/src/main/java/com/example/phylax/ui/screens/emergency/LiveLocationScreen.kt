package com.example.phylax.ui.screens.emergency

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.example.phylax.ui.theme.*
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission", "SetJavaScriptEnabled")
@Composable
fun LiveLocationScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var lat by remember { mutableDoubleStateOf(14.5995) } 
    var lon by remember { mutableDoubleStateOf(120.9842) }

    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                lat = it.latitude
                lon = it.longitude
            }
        }
    }

    val mapHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                body { margin: 0; padding: 0; background-color: #f2f2f7; }
                #map { height: 100vh; width: 100vw; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map = L.map('map', { zoomControl: false }).setView([$lat, $lon], 16);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
                var marker = L.marker([$lat, $lon]).addTo(map);
            </script>
        </body>
        </html>
    """.trimIndent()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        // Enhanced WebView for Prototype Map
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }
                    webViewClient = WebViewClient()
                    loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                // Only reload if the coordinates have actually changed significantly to avoid flickering
                webView.loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null)
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // iOS 18 Floating Header
        Surface(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .height(44.dp),
            shape = RoundedCornerShape(22.dp),
            color = Color.White.copy(alpha = 0.9f),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(iOSGreen, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "PROTOTYPE MAP ACTIVE", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }

        // Floating Back Button
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .statusBarsPadding()
                .size(44.dp)
                .align(Alignment.TopStart),
            shape = CircleShape,
            color = Color.White,
            onClick = onBack,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = iOSBlue)
            }
        }
        
        // Floating Card
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.05f)),
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = iOSGreen.copy(alpha = 0.1f)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.padding(12.dp), tint = iOSGreen)
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(text = "Current Status", style = MaterialTheme.typography.bodyLarge.copy(color = SecondaryText))
                        Text(text = "Tracking active", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Button(
                    onClick = {
                        val gmmIntentUri = "geo:$lat,$lon?q=$lat,$lon".toUri()
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = iOSBlue)
                ) {
                    Text("Verify in Full Maps", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            }
        }
    }
}
