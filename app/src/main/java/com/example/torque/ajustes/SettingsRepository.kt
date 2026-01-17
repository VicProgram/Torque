package com.example.torque.ajustes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    val notificationsEnabledFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[SettingsKeys.NOTIFICATIONS] ?: true }

    val darkModeEnabledFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[SettingsKeys.DARK_MODE] ?: true }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.NOTIFICATIONS] = enabled
        }
    }

    suspend fun setDarkModeEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.DARK_MODE] = enabled
        }
    }
}