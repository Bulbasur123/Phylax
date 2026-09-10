package com.example.phylax.ui.screens.auth

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phylax.data.model.User
import com.example.phylax.data.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    private var verificationId: String? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var pendingUser: User? = null
    private var pendingPassword: String? = null

    fun onEvent(event: AuthUserEvent) {
        when (event) {
            is AuthUserEvent.RegisterWithEmail -> registerWithEmail(event.user, event.password)
            is AuthUserEvent.LoginWithEmail -> loginWithEmail(event.email, event.password)
            is AuthUserEvent.ForgotPassword -> forgotPassword(event.email)
            is AuthUserEvent.SendOtp -> sendOtp(event.phoneNumber, event.activity, event.user, event.password)
            is AuthUserEvent.ResendOtp -> resendOtp(event.phoneNumber, event.activity)
            is AuthUserEvent.VerifyOtp -> verifyOtp(event.otp)
            is AuthUserEvent.Logout -> logout()
        }
    }

    private fun registerWithEmail(user: User, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = authRepository.registerWithEmail(user, password)
            uiState = uiState.copy(isLoading = false, user = result.getOrNull())
            result.onSuccess {
                _events.emit(AuthEvent.AuthSuccess)
            }.onFailure {
                _events.emit(AuthEvent.Error(it.message ?: "Registration failed"))
            }
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        Log.d("AuthViewModel", "Attempting login with email: $email")
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = authRepository.loginWithEmail(email, password)
            uiState = uiState.copy(isLoading = false, user = result.getOrNull())
            result.onSuccess {
                Log.d("AuthViewModel", "Login successful for: $email")
                _events.emit(AuthEvent.AuthSuccess)
            }.onFailure {
                Log.e("AuthViewModel", "Login failed: ${it.message}")
                _events.emit(AuthEvent.Error(it.message ?: "Login failed"))
            }
        }
    }

    private fun forgotPassword(email: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = authRepository.sendPasswordResetEmail(email)
            uiState = uiState.copy(isLoading = false)
            result.onSuccess {
                _events.emit(AuthEvent.PasswordResetSent)
            }.onFailure {
                _events.emit(AuthEvent.Error(it.message ?: "Failed to send reset email"))
            }
        }
    }

    private fun sendOtp(phoneNumber: String, activity: Activity, user: User? = null, password: String? = null) {
        // Enforce 11-digit PH mobile number check (e.g., 09123456789)
        if (phoneNumber.length != 11 || !phoneNumber.startsWith("0")) {
            viewModelScope.launch {
                _events.emit(AuthEvent.Error("Please enter a valid 11-digit mobile number"))
            }
            return
        }

        // Convert 0917... to +63917... for Firebase SMS
        val formattedPhone = "+63" + phoneNumber.substring(1)

        uiState = uiState.copy(isLoading = true)
        pendingUser = user
        pendingPassword = password
        
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("AuthViewModel", "onVerificationCompleted")
                viewModelScope.launch {
                    signInWithCredential(credential)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("AuthViewModel", "onVerificationFailed: ${e.message}")
                uiState = uiState.copy(isLoading = false)
                viewModelScope.launch {
                    _events.emit(AuthEvent.Error(e.message ?: "Verification failed"))
                }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d("AuthViewModel", "onCodeSent: $id")
                uiState = uiState.copy(isLoading = false)
                verificationId = id
                resendingToken = token
                viewModelScope.launch {
                    _events.emit(AuthEvent.OtpSent(formattedPhone))
                }
            }
        }

        authRepository.sendOtp(formattedPhone, activity, callbacks)
    }

    private fun resendOtp(phoneNumber: String, activity: Activity) {
        uiState = uiState.copy(isLoading = true)
        
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                viewModelScope.launch {
                    signInWithCredential(credential)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                uiState = uiState.copy(isLoading = false)
                viewModelScope.launch {
                    _events.emit(AuthEvent.Error(e.message ?: "Resend failed"))
                }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                uiState = uiState.copy(isLoading = false)
                verificationId = id
                resendingToken = token
                viewModelScope.launch {
                    _events.emit(AuthEvent.OtpSent(phoneNumber))
                }
            }
        }

        authRepository.sendOtp(phoneNumber, activity, callbacks, resendingToken)
    }

    private fun verifyOtp(otp: String) {
        val id = verificationId ?: return
        val credential = PhoneAuthProvider.getCredential(id, otp)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            
            if (pendingPassword != null && pendingUser != null) {
                // This is a registration flow: verify OTP first, then create Email/Pass account
                val phoneResult = authRepository.signInWithCredential(credential, null)
                if (phoneResult.isSuccess) {
                    // Phone verified! Now create the actual Email/Pass credentials
                    registerWithEmail(pendingUser!!, pendingPassword!!)
                } else {
                    uiState = uiState.copy(isLoading = false)
                    _events.emit(AuthEvent.Error(phoneResult.exceptionOrNull()?.message ?: "Verification failed"))
                }
            } else {
                // Standard phone-only login (if used)
                val result = authRepository.signInWithCredential(credential, pendingUser)
                uiState = uiState.copy(isLoading = false, user = result.getOrNull())
                result.onSuccess {
                    _events.emit(AuthEvent.AuthSuccess)
                }.onFailure {
                    _events.emit(AuthEvent.Error(it.message ?: "Login failed"))
                }
            }
        }
    }

    private fun logout() {
        authRepository.logout()
        uiState = uiState.copy(user = null)
        viewModelScope.launch {
            _events.emit(AuthEvent.LoggedOut)
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null
)

sealed class AuthEvent {
    object AuthSuccess : AuthEvent()
    object LoggedOut : AuthEvent()
    object PasswordResetSent : AuthEvent()
    data class OtpSent(val phoneNumber: String) : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}

sealed class AuthUserEvent {
    data class RegisterWithEmail(val user: User, val password: String) : AuthUserEvent()
    data class LoginWithEmail(val email: String, val password: String) : AuthUserEvent()
    data class ForgotPassword(val email: String) : AuthUserEvent()
    data class SendOtp(val phoneNumber: String, val activity: Activity, val user: User? = null, val password: String? = null) : AuthUserEvent()
    data class ResendOtp(val phoneNumber: String, val activity: Activity) : AuthUserEvent()
    data class VerifyOtp(val otp: String) : AuthUserEvent()
    object Logout : AuthUserEvent()
}
