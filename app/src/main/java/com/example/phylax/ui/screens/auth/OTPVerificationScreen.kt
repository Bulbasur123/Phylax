package com.example.phylax.ui.screens.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.phylax.ui.components.PremiumButton
import com.example.phylax.ui.theme.Background
import com.example.phylax.ui.theme.BorderColor
import com.example.phylax.ui.theme.PrimaryText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OTPVerificationScreen(
    phoneNumber: String,
    onVerifySuccess: () -> Unit,
    viewModel: AuthViewModel
) {
    var otp by remember { mutableStateOf("") }
    var ticks by remember { mutableIntStateOf(60) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Helper to find Activity
    fun findActivity(context: Context): Activity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }
        return null
    }

    LaunchedEffect(ticks) {
        if (ticks > 0) {
            delay(1000)
            ticks--
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.AuthSuccess -> onVerifySuccess()
                is AuthEvent.OtpSent -> {
                    ticks = 60
                    snackbarHostState.showSnackbar("New code sent")
                }
                is AuthEvent.Error -> snackbarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verification",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Text(
                text = "We sent a code to $phoneNumber",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            BasicTextField(
                value = otp,
                onValueChange = { if (it.length <= 6) otp = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(6) { index ->
                            val char = otp.getOrNull(index)?.toString() ?: ""
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .border(1.dp, if (char.isNotEmpty()) PrimaryText else BorderColor, RoundedCornerShape(8.dp))
                                    .background(Color.White, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText
                                    )
                                )
                            }
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            PremiumButton(
                text = "VERIFY",
                onClick = {
                    if (otp.length == 6) {
                        viewModel.onEvent(AuthUserEvent.VerifyOtp(otp))
                    } else {
                        // Show local error if digits are missing
                        scope.launch { snackbarHostState.showSnackbar("Please enter all 6 digits") }
                    }
                },
                enabled = !viewModel.uiState.isLoading
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (ticks > 0) {
                Text(
                    text = "Resend code in 00:${ticks.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                )
            } else {
                TextButton(
                    onClick = {
                        val activity = findActivity(context)
                        if (activity != null) {
                            viewModel.onEvent(AuthUserEvent.ResendOtp(phoneNumber, activity))
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Unable to resend code") }
                        }
                    },
                    enabled = !viewModel.uiState.isLoading
                ) {
                    Text("RESEND CODE", color = PrimaryText, fontWeight = FontWeight.Bold)
                }
            }
            
            if (viewModel.uiState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}
