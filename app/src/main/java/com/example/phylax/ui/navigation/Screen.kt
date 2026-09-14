package com.example.phylax.ui.navigation

sealed class Screen(val route: String) {
    // Auth Flow
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Register : Screen("register")
    object OTP : Screen("otp/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp/$phoneNumber"
    }

    // Setup Flow
    object ContactsSetup : Screen("contacts_setup")
    object Permissions : Screen("permissions")
    object SafetySetup : Screen("safety_setup")

    // Main App
    object Home : Screen("home")
    object SafetyTips : Screen("safety_tips")
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    object About : Screen("about")
    object LoudAlarm : Screen("loud_alarm")
    object Chat : Screen("chat/{userId}/{userName}") {
        fun createRoute(userId: String, userName: String) = "chat/$userId/$userName"
    }

    // Emergency
    object SOSActivation : Screen("sos_activation")
    object EmergencyDashboard : Screen("emergency_dashboard")
    object LiveLocation : Screen("live_location")
}
