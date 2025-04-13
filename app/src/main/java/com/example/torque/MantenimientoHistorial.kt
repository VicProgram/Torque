package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.TorqueTheme

data class MantenimientoRealizado(
    val nombre: String,
    val fecha: String,
    val kilometros: Int
)

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
    val historial = listOf(
        MantenimientoRealizado("Cambio de aceite", "2024-12-15", 10500),
        MantenimientoRealizado("Cambio filtro aire", "2024-11-01", 9800),
        MantenimientoRealizado("Pastillas delanteras", "2024-09-20", 9200),
        MantenimientoRealizado("Revisión general", "2024-06-10", 8500),
    )

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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(historial) { item ->
                MantenimientoHistorialCard(item)
            }
        }
    }
}

@Composable
fun MantenimientoHistorialCard(item: MantenimientoRealizado) {
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
