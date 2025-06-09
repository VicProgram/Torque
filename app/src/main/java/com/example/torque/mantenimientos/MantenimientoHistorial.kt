package com.example.torque.mantenimientos

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// ---------- Modelo de datos ----------
data class MantenimientoDetalle(
    val mantenimientoId: Int = 0,
    val nombreMantenimiento: String,
    val date: String,
    val kilometers: Int?,
    val marca: String?,
    val modelo: String?,
    val anno: Int?,
    val precio: String?,
    val tiempo: String?,
    val notas: String?,
    val tareas: String?
)

// ---------- ViewModel ----------
class MantenimientoHistorialView(application: Application) : AndroidViewModel(application) {
    private val dbHelper = TorqueDatabaseHelper(application)

    private val _historial = MutableStateFlow<List<MantenimientoDetalle>>(emptyList())
    val historial: StateFlow<List<MantenimientoDetalle>> = _historial

    fun cargarHistorial() {
        viewModelScope.launch {
            _historial.value = dbHelper.obtenerMantenimientos()
        }
    }
}

// ---------- Activity principal (una sola pantalla) ----------
class MantenimientoHistorial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MantenimientoHistorialPantalla()
            }
        }
    }
}

// ---------- Pantalla unificada ----------
@Composable
fun MantenimientoHistorialPantalla() {
    val viewModel: MantenimientoHistorialView = viewModel()
    val historial = viewModel.historial.collectAsState()

    // Estado para controlar qué mantenimiento está seleccionado
    val selectedMantenimiento = remember { mutableStateOf<MantenimientoDetalle?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial()
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

        if (historial.value.isEmpty()) {
            Text(
                text = "No hay mantenimientos registrados.",
                color = Color.LightGray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(historial.value) { item ->
                    HistorialItemCard(
                        nombre = item.nombreMantenimiento,
                        fecha = item.date,
                        kilometros = item.kilometers ?: 0,
                        onClick = {
                            selectedMantenimiento.value = item
                        }
                    )
                }
            }
        }

        // Mostrar detalles si hay uno seleccionado
        selectedMantenimiento.value?.let { mantenimiento ->
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Detalles del Mantenimiento",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nombre: ${mantenimiento.nombreMantenimiento}", color = Color.Gray)
                    Text("Fecha: ${mantenimiento.date}", color = Color.Gray)
                    Text("Km: ${mantenimiento.kilometers ?: "N/D"}", color = Color.Gray)
                    Text(
                        "Moto: ${mantenimiento.marca ?: ""} ${mantenimiento.modelo ?: ""} (${mantenimiento.anno ?: ""})",
                        color = Color.Gray
                    )
                    Text("Precio: ${mantenimiento.precio ?: "N/D"}", color = Color.Gray)
                    Text("Tiempo: ${mantenimiento.tiempo ?: "N/D"}", color = Color.Gray)
                    Text("Notas: ${mantenimiento.notas ?: "N/D"}", color = Color.Gray)
                    Text("Tareas: ${mantenimiento.tareas ?: "N/D"}", color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { selectedMantenimiento.value = null }) {
                        Text("Cerrar", color = Color.White)
                    }
                }
            }
        }
    }
}

// ---------- Card de cada item ----------
@Composable
fun HistorialItemCard(
    nombre: String,
    fecha: String,
    kilometros: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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


