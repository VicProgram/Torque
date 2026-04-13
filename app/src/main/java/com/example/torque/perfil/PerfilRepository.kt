package com.example.torque.perfil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ClavesPerfil {
    val NOMBRE = stringPreferencesKey("perfil_nombre")
    val FECHA_NACIMIENTO = stringPreferencesKey("perfil_fecha_nacimiento_iso") // YYYY-MM-DD
}

class PerfilRepository(private val dataStore: DataStore<Preferences>) {

    val nombreFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ClavesPerfil.NOMBRE]
    }

    val fechaNacimientoFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ClavesPerfil.FECHA_NACIMIENTO]
    }

    suspend fun establecerNombre(nombre: String) {
        dataStore.edit { prefs -> prefs[ClavesPerfil.NOMBRE] = nombre }
    }

    suspend fun establecerFechaNacimientoIso(fechaIso: String) {
        dataStore.edit { prefs -> prefs[ClavesPerfil.FECHA_NACIMIENTO] = fechaIso }
    }
}
