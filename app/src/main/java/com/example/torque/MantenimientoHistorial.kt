package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.database.MiGarajeDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

// Clase de datos para el mantenimiento


class MantenimientoHistorial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HistorialScreen()
                }
            }
        }
    }
}



@Composable
fun HistorialScreen() {
    // Obtener la base de datos de manera local
    val dbHelper = MiGarajeDatabaseHelper(LocalContext.current)
    val mantenimientos = remember { mutableStateListOf<mantenimientos>() }

    // Cargar los datos de la base de datos
    LaunchedEffect(Unit) {
        mantenimientos.addAll(dbHelper.ObtenerMantenimientos())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de Mantenimientos",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar los mantenimientos en un LazyColumn
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mantenimientos) { item ->
                MantenimientoHistorialCard(item)
            }
        }
    }
}

@Composable
fun MantenimientoHistorialCard(item: mantenimientos) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = item.nombre, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text(text = "Fecha: ${item.fecha}", color = Color.LightGray)
            Text(text = "Km: ${item.kilometros}", color = Color.LightGray)
        }
    }
}

