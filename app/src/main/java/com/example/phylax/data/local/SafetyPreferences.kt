package com.example.phylax.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "safety_settings")

class SafetyPreferences(private val context: Context) {

    companion object {
        val EMERGENCY_SOS = booleanPreferencesKey("emergency_sos")
        val LIVE_LOCATION = booleanPreferencesKey("live_location")
        val LOUD_ALARM = booleanPreferencesKey("loud_alarm")
        val FAKE_CALL = booleanPreferencesKey("fake_call")
    }

    val sosEnabled: Flow<Boolean> = context.dataStore.data.map { it[EMERGENCY_SOS] ?: true }
    val liveLocationEnabled: Flow<Boolean> = context.dataStore.data.map { it[LIVE_LOCATION] ?: true }
    val alarmEnabled: Flow<Boolean> = context.dataStore.data.map { it[LOUD_ALARM] ?: true }
    val fakeCallEnabled: Flow<Boolean> = context.dataStore.data.map { it[FAKE_CALL] ?: false }

    suspend fun setSosEnabled(enabled: Boolean) = context.dataStore.edit { it[EMERGENCY_SOS] = enabled }
    suspend fun setLiveLocationEnabled(enabled: Boolean) = context.dataStore.edit { it[LIVE_LOCATION] = enabled }
    suspend fun setAlarmEnabled(enabled: Boolean) = context.dataStore.edit { it[LOUD_ALARM] = enabled }
    suspend fun setFakeCallEnabled(enabled: Boolean) = context.dataStore.edit { it[FAKE_CALL] = enabled }
}
