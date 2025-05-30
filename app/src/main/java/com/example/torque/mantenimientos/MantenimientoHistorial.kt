package com.example.torque.mantenimientos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

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
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }

    // Estado que guarda la lista de mantenimientos
    var historial by remember { mutableStateOf<List<MantenimientoDetalle>>(emptyList()) }

    // Carga inicial y recarga cuando el Composable entra en composición
    LaunchedEffect(Unit) {
        historial = dbHelper.obtenerMantenimientos()
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

        if (historial.isEmpty()) {
            Text(
                text = "No hay mantenimientos registrados.",
                color = Color.LightGray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(historial) { item ->
                    HistorialItemCard(
                        nombre = item.nombreMantenimiento,
                        fecha = item.date,
                        kilometros = item.kilometers ?: 0
                    )
                }
            }
        }
    }
}

@Composable
fun HistorialItemCard(nombre: String, fecha: String, kilometros: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombre, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha: $fecha", color = Color.Gray)
            Text(text = "Km: $kilometros", color = Color.Gray)
        }
    }
}
