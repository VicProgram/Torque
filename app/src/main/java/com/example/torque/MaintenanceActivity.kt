package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    val items = listOf(
        MaintenanceItem("Cambio de aceite"),
        MaintenanceItem("Frenado"),
        MaintenanceItem("Neumáticos"),
        MaintenanceItem("Filtro de Aire"),
        MaintenanceItem("Revisión de luces"),
        MaintenanceItem("Batería"),
        MaintenanceItem("Suspensión"),
        MaintenanceItem("Cambio de bujías")
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.fondomanwebp), // Asegurate de tener la imagen en drawable
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura encima
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Contenido (rejilla de botones)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items.size) { index ->
                MaintenanceItemCard(item = items[index])
            }
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
        // Contenedor para centrar el texto
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Centra el texto dentro del Button
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White // Asegúrate de que el texto sea legible
            )
        }
    }
}










