package com.example.torque.mantenimientos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.R
import com.example.torque.ui.theme.TorqueTheme

data class MaintenanceItem(val name: String)

class Mantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MaintenanceScreen()
            }
        }
    }
}

@Composable
fun MaintenanceScreen() {
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

    val checkedItems = remember { mutableStateMapOf<String, Boolean>() }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondomanwebp),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                secciones.forEach { (titulo, lista) ->
                    item {
                        Text(
                            text = titulo,
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(lista) { item ->
                        val isChecked = checkedItems[item.name] ?: false
                        MaintenanceItemCard(
                            name = item.name,
                            isChecked = isChecked,
                            onCheckedChange = { checked ->
                                checkedItems[item.name] = checked
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val seleccionados = checkedItems.filterValues { it }.keys.toList()
                    if (seleccionados.isNotEmpty()) {
                        val intent = Intent(context, MantenimientoFormulario::class.java)
                        intent.putStringArrayListExtra("seleccionados", ArrayList(seleccionados))
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Guardar Mantenimiento")
            }
        }
    }
}

@Composable
fun MaintenanceItemCard(
    name: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan.copy(alpha = 0.25f), shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF4CAF50),
                uncheckedColor = Color.LightGray,
                checkmarkColor = Color.Black
            )
        )
    }
    
}
