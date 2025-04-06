package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.TorqueTheme

// Data class para representar cada elemento
data class MaintenanceItem(val name: String)

class MaintenanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MaintenanceGrid()
            }
        }
    }
}

@Composable
fun MaintenanceGrid() {
    // Lista de elementos con los nombres que desees (botones de mantenimiento)
    val items = listOf(
        MaintenanceItem("Cambio de aceite"),
        MaintenanceItem("Revisión de frenos"),
        MaintenanceItem("Revisión de neumáticos"),
        MaintenanceItem("Cambio de filtro"),
        MaintenanceItem("Revisión de luces"),
        MaintenanceItem("Chequeo de batería"),
        MaintenanceItem("Revisión de suspensión"), // Agregado ejemplo adicional
        MaintenanceItem("Cambio de bujías") // Agregado ejemplo adicional
    )

    // Usamos LazyVerticalGrid con 2 columnas
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Dos columnas
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items.size) { index ->
            MaintenanceItemCard(item = items[index])
        }
    }
}

@Composable
fun MaintenanceItemCard(item: MaintenanceItem) {
    // Cada card tendrá el nombre del mantenimiento
    Button(
        onClick = { /* Acción cuando se haga clic en el botón */ },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)  // Aseguramos que el botón tenga una altura definida
    ) {
        Text(text = item.name, style = MaterialTheme.typography.labelMedium)
    }
}
