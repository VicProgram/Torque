package com.example.torque.ajustes

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
    }

    val notificationsEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val darkModeEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_MODE_ENABLED] ?: true
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setDarkModeEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE_ENABLED] = enabled
        }
    }
}