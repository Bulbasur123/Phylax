package com.example.phylax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.phylax.ui.screens.auth.*
import com.example.phylax.ui.screens.emergency.*
import com.example.phylax.ui.screens.home.*
import com.example.phylax.ui.screens.setup.*

@Composable
fun PhylaxNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    
    // Start at Home if user is already logged in, otherwise Welcome
    val startDestination = if (currentUser != null) Screen.Home.route else Screen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onGetStarted = { navController.navigate(Screen.Login.route) })
        }
        
        composable(Screen.Register.route) {
            RegistrationScreen(
                onOtpSent = { phone -> 
                    // Restoration: Navigate to OTP screen so user can enter the "Key"
                    navController.navigate(Screen.OTP.createRoute(phone))
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                viewModel = authViewModel
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                viewModel = authViewModel
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }
        
        composable(
            route = Screen.OTP.route,
            arguments = listOf(navArgument("phoneNumber") { defaultValue = "" })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OTPVerificationScreen(
                phoneNumber = phone,
                onVerifySuccess = { navController.navigate(Screen.ContactsSetup.route) },
                viewModel = authViewModel
            )
        }
        
        composable(Screen.ContactsSetup.route) {
            EmergencyContactsScreen(
                onContinue = { navController.navigate(Screen.Permissions.route) },
                onBack = { navController.popBackStack() },
                onNavigateToChat = { id, name ->
                    navController.navigate(Screen.Chat.createRoute(id, name))
                }
            )
        }
        
        composable(Screen.Permissions.route) {
            PermissionScreen(
                onAllow = { navController.navigate(Screen.SafetySetup.route) },
                onNotNow = { navController.navigate(Screen.SafetySetup.route) }
            )
        }
        
        composable(Screen.SafetySetup.route) {
            SafetySettingsScreen(onFinish = { navController.navigate(Screen.Home.route) })
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSOS = { navController.navigate(Screen.EmergencyDashboard.route) },
                onNavigateToContacts = { navController.navigate(Screen.ContactsSetup.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAlarm = { navController.navigate(Screen.LoudAlarm.route) },
                onNavigateToLocation = { navController.navigate(Screen.LiveLocation.route) }
            )
        }

        composable(Screen.LoudAlarm.route) {
            LoudAlarmScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.SOSActivation.route) {
            EmergencyActivationScreen(
                onCountdownFinished = { navController.navigate(Screen.EmergencyDashboard.route) },
                onCancel = { navController.popBackStack() }
            )
        }
        
        composable(Screen.EmergencyDashboard.route) {
            EmergencyDashboard(
                userId = authViewModel.uiState.user?.uid ?: "",
                onStopSOS = { navController.navigate(Screen.Home.route) },
                onViewMap = { navController.navigate(Screen.LiveLocation.route) }
            )
        }
        
        composable(Screen.LiveLocation.route) {
            LiveLocationScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.History.route) { 
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onRecordClick = { session ->
                    navController.navigate(Screen.HistoryDetail.route)
                }
            ) 
        }

        composable(Screen.HistoryDetail.route) {
            HistoryDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SafetyTips.route) { 
            SafetyTipsScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.Settings.route) { 
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.onEvent(AuthUserEvent.Logout)
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToContacts = { navController.navigate(Screen.ContactsSetup.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToPermissions = { navController.navigate(Screen.Permissions.route) },
                onNavigateToSafetySetup = { navController.navigate(Screen.SafetySetup.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) }
            ) 
        }

        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("userId") { defaultValue = "" },
                navArgument("userName") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ChatScreen(
                otherUserId = userId,
                otherUserName = userName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
