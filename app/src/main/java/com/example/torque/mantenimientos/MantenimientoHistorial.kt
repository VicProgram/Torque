package com.example.torque.mantenimientos

import com.example.torque.ui.theme.TorqueTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
