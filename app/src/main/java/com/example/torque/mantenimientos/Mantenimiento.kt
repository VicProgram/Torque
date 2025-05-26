package com.example.torque.mantenimientos

import com.example.torque.ui.theme.TorqueTheme
import androidx.compose.foundation.lazy.items
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.R
import com.example.torque.ui.theme.MaintenanceItemCard

// Data class para representar cada elemento
data class MaintenanceItem(val name: String)

class Mantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TorqueTheme {
                MaintenanceColumn()

            }
        }
    }
}

@Composable
fun MaintenanceColumn() {

    val motorItems = listOf(
        MaintenanceItem("Cambio de aceite"),
        MaintenanceItem("Cambio filtro aceite"),
        MaintenanceItem("Cambio filtro aire"),
        MaintenanceItem("Cambio filtro de combustible"),
        MaintenanceItem("Cambio de bujías"),
        MaintenanceItem("Revisión líquido refrigerante")
    )

    val ruedasFrenosItems = listOf(
        MaintenanceItem("Neumático delantero"),
        MaintenanceItem("Neumático trasero"),
        MaintenanceItem("Pastillas delanteras"),
        MaintenanceItem("Pastillas traseras"),
        MaintenanceItem("Cambio líquido frenos"),
        MaintenanceItem("Presión neumáticos al guardar")
    )

    val transmisionItems = listOf(
        MaintenanceItem("Engrase de cadena"),
        MaintenanceItem("Tensión de cadena"),
        MaintenanceItem("Cambio de kit de arrastre")
    )

    val electricoItems = listOf(
        MaintenanceItem("Luces posición"),
        MaintenanceItem("Luces carretera"),
        MaintenanceItem("Luces largo alcance"),
        MaintenanceItem("Luces intermitentes"),
        MaintenanceItem("Batería"),
        MaintenanceItem("Revisión fusibles"),
        MaintenanceItem("Revisión cableado eléctrico")
    )

    val otrosItems = listOf(
        MaintenanceItem("Acelerador"),
        MaintenanceItem("Revisión Manetas"),
        MaintenanceItem("Ajuste manetas de freno y embrague"),
        MaintenanceItem("Revisión tornillería general"),
        MaintenanceItem("Suspensión"),
        MaintenanceItem("Preparación para invierno"),
        MaintenanceItem("Revisión post-invierno"),
        MaintenanceItem("Cuidado durante periodos de inactividad")
    )

    val secciones = listOf(
        "Motor" to motorItems,
        "Ruedas y Frenos" to ruedasFrenosItems,
        "Transmisión" to transmisionItems,
        "Sistema Eléctrico" to electricoItems,
        "Otros" to otrosItems
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
        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            secciones.forEach { (titulo, lista) ->
                // Título de la sección
                item {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                // Elementos dentro de esa sección
                items(lista) { item ->
                    MaintenanceItemCard(item = item)
                }
            }
        }
    }
}