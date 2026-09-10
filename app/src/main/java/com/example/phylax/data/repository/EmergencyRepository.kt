package com.example.phylax.data.repository

import com.example.phylax.data.model.EmergencyContact
import com.example.phylax.data.model.EmergencySession
import com.example.phylax.data.model.LocationUpdate
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EmergencyRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun addContact(contact: EmergencyContact): Result<Unit> {
        return try {
            val doc = firestore.collection("emergencyContacts").document()
            firestore.collection("emergencyContacts").document(doc.id)
                .set(contact.copy(id = doc.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getContacts(userId: String): Result<List<EmergencyContact>> {
        return try {
            val snapshot = firestore.collection("emergencyContacts")
                .whereEqualTo("userId", userId)
                .get().await()
            val contacts = snapshot.toObjects(EmergencyContact::class.java)
            Result.success(contacts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteContact(contactId: String): Result<Unit> {
        return try {
            firestore.collection("emergencyContacts").document(contactId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateContact(contact: EmergencyContact): Result<Unit> {
        return try {
            firestore.collection("emergencyContacts").document(contact.id).set(contact).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startEmergencySession(userId: String, lat: Double, lon: Double): Result<String> {
        return try {
            val doc = firestore.collection("emergencySessions").document()
            val session = EmergencySession(
                id = doc.id,
                userId = userId,
                latitude = lat,
                longitude = lon
            )
            doc.set(session).await()
            Result.success(doc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLocation(sessionId: String, lat: Double, lon: Double): Result<Unit> {
        return try {
            val update = LocationUpdate(
                sessionId = sessionId,
                latitude = lat,
                longitude = lon
            )
            firestore.collection("locationUpdates").add(update).await()
            
            firestore.collection("emergencySessions").document(sessionId)
                .update("latitude", lat, "longitude", lon).await()
                
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEmergencyHistory(userId: String): Result<List<EmergencySession>> {
        return try {
            val snapshot = firestore.collection("emergencySessions")
                .whereEqualTo("userId", userId)
                .get().await()
            val sessions = snapshot.toObjects(EmergencySession::class.java)
                .sortedByDescending { it.startedAt }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSessionNote(sessionId: String, note: String): Result<Unit> {
        return try {
            firestore.collection("emergencySessions").document(sessionId)
                .update("note", note).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSession(sessionId: String): Result<Unit> {
        return try {
            firestore.collection("emergencySessions").document(sessionId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endEmergencySession(sessionId: String): Result<Unit> {
        return try {
            firestore.collection("emergencySessions").document(sessionId)
                .update("status", "ENDED", "endedAt", System.currentTimeMillis()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
