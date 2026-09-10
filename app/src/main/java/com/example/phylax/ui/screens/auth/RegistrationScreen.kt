package com.example.phylax.ui.screens.auth

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.phylax.data.model.User
import com.example.phylax.ui.components.PremiumButton
import com.example.phylax.ui.components.PremiumTextField
import com.example.phylax.ui.theme.Background
import com.example.phylax.ui.theme.BorderColor
import com.example.phylax.ui.theme.PrimaryText

@Composable
fun RegistrationScreen(
    onOtpSent: (String) -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.OtpSent -> onOtpSent(event.phoneNumber)
                is AuthEvent.AuthSuccess -> {
                    // This will now be handled after OTP verification
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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Text(
                text = "Join Phylax for your safety",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            PremiumTextField(value = name, onValueChange = { name = it }, label = "Full Name", icon = Icons.Default.Person)
            PremiumTextField(
                value = phone, 
                onValueChange = { 
                    if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                        phone = it 
                    }
                }, 
                label = "Phone Number", 
                icon = Icons.Default.Phone, 
                keyboardType = KeyboardType.Phone
            )
            PremiumTextField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
            
            PremiumTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )
            
            PremiumTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            PremiumButton(
                text = "CREATE ACCOUNT",
                onClick = {
                    if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        if (password == confirmPassword) {
                            val user = User(name = name, phone = phone, email = email)
                            // Start by sending OTP. Store password in a custom event if needed, 
                            // but for now we store it in the ViewModel's pending state.
                            viewModel.onEvent(AuthUserEvent.SendOtp(phone, context as Activity, user, password))
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
                        }
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Please fill in all fields") }
                    }
                },
                enabled = !viewModel.uiState.isLoading
            )
            
            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("ALREADY HAVE AN ACCOUNT? LOG IN", color = PrimaryText)
            }
            
            if (viewModel.uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
