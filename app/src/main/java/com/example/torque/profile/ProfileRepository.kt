package com.example.torque.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ProfileKeys {
    val NAME = stringPreferencesKey("profile_name")
    val BIRTHDAY = stringPreferencesKey("profile_birthday_iso") // YYYY-MM-DD
}

class ProfileRepository(private val dataStore: DataStore<Preferences>) {

    val nameFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ProfileKeys.NAME]
    }

    val birthdayFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ProfileKeys.BIRTHDAY]
    }

    suspend fun setName(name: String) {
        dataStore.edit { prefs -> prefs[ProfileKeys.NAME] = name }
    }

    suspend fun setBirthdayIso(isoDate: String) {
        dataStore.edit { prefs -> prefs[ProfileKeys.BIRTHDAY] = isoDate }
    }
}
