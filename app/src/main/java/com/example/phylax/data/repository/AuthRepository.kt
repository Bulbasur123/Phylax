package com.example.phylax.data.repository

import android.app.Activity
import com.example.phylax.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getCurrentUser() = auth.currentUser

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    ) {
        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        
        forceResendingToken?.let {
            builder.setForceResendingToken(it)
        }
        
        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential, userProfile: User? = null): Result<User> {
        return try {
            val result = auth.signInWithCredential(credential).await()
            val uid = result.user?.uid ?: ""
            
            val user = if (userProfile != null) {
                // Registration: Save new profile
                val finalUser = userProfile.copy(uid = uid)
                withTimeout(10000) { // 10 second timeout for Firestore
                    firestore.collection("users").document(uid).set(finalUser).await()
                }
                finalUser
            } else {
                // Login: Fetch existing profile
                withTimeout(10000) {
                    val doc = firestore.collection("users").document(uid).get().await()
                    doc.toObject(User::class.java) ?: throw Exception("User profile not found")
                }
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerWithEmail(user: User, password: String): Result<User> {
        return try {
            // 1. Try to create user
            val result = try {
                auth.createUserWithEmailAndPassword(user.email, password).await()
            } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                // If user already exists (maybe from a previous timed-out attempt), try logging in
                auth.signInWithEmailAndPassword(user.email, password).await()
            }
            
            val uid = result.user?.uid ?: ""
            
            // 2. Save/Update Profile in Firestore
            val finalUser = user.copy(uid = uid)
            withTimeout(15000) { // Increased timeout to 15s
                firestore.collection("users").document(uid).set(finalUser).await()
            }
            Result.success(finalUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: ""
            val user = withTimeout(10000) {
                val doc = firestore.collection("users").document(uid).get().await()
                doc.toObject(User::class.java) ?: throw Exception("User profile not found")
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()
}
