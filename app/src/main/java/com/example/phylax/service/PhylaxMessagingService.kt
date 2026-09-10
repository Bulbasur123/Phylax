package com.example.phylax.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.phylax.MainActivity
import com.example.phylax.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PhylaxMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        val title = remoteMessage.notification?.title ?: "SOS Alert"
        val body = remoteMessage.notification?.body ?: "Someone needs help."
        
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val userPhone = "+63994843687" // Example: This would come from the FCM data payload

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("is_emergency_alert", true)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // "Call User" Action
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$userPhone"))
        val callPendingIntent = PendingIntent.getActivity(
            this, 2, callIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "emergency_alerts"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_call, "CALL USER", callPendingIntent)
            .addAction(android.R.drawable.ic_menu_mylocation, "VIEW LOCATION", pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Emergency Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority emergency alerts"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500) // Strong SOS-like pattern
                setBypassDnd(true) // Attempt to bypass Do Not Disturb
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to Firestore for the user
    }
}
