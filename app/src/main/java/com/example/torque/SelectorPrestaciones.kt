package com.example.torque

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
import com.example.torque.ui.theme.TorqueTheme
import com.example.torque.ui.theme.MaintenanceItemCard

// Data class para representar cada elemento
//data class Prestacion(val name: String)



class Prestaciones : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TorqueTheme {
                Prestación()
            }
        }
    }
}
data class Prestacion(val nombre: String)

@Composable
fun Prestación() {

    val motorItems = listOf(
        Prestación("Cambio de aceite"),
        Prestación("Cambio filtro aceite"),
        Prestación("Cambio filtro aire"),
        Prestación("Cambio filtro de combustible"),
        Prestación("Cambio de bujías"),
        Prestación("Revisión líquido refrigerante")
    )

    val ruedasFrenosItems = listOf(
        Prestación("Neumático delantero"),
        Prestación("Neumático trasero"),
        Prestación("Pastillas delanteras"),
        Prestación("Pastillas traseras"),
        Prestación("Cambio líquido frenos"),
        Prestación("Presión neumáticos al guardar")
    )

    val transmisionItems = listOf(
        Prestación("Engrase de cadena"),
        Prestación("Tensión de cadena"),
        Prestación("Cambio de kit de arrastre")
    )

    val electricoItems = listOf(
        Prestación("Luces posición"),
        Prestación("Luces carretera"),
        Prestación("Luces largo alcance"),
        Prestación("Luces intermitentes"),
        Prestación("Batería"),
        Prestación("Revisión fusibles"),
        Prestación("Revisión cableado eléctrico")
    )

    val otrosItems = listOf(
        Prestación("Acelerador"),
        Prestación("Revisión Manetas"),
        Prestación("Ajuste manetas de freno y embrague"),
        Prestación("Revisión tornillería general"),
        Prestación("Suspensión"),
        Prestación("Preparación para invierno"),
        Prestación("Revisión post-invierno"),
        Prestación("Cuidado durante periodos de inactividad")
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