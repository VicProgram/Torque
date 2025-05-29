package com.example.torque.mantenimientos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.R
import com.example.torque.ui.theme.MantenimientoItemCard
import com.example.torque.ui.theme.TorqueTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MaintenanceItem(
    val id: Int,
    val name: String,
    var isChecked: Boolean = false
)

class MaintenanceViewModel : ViewModel() {

    private val _sections = MutableStateFlow<List<Pair<String, List<MaintenanceItem>>>>(emptyList())
    val sections: StateFlow<List<Pair<String, List<MaintenanceItem>>>> = _sections

    init {
        loadMaintenanceItems()
    }

    private fun loadMaintenanceItems() {
        viewModelScope.launch {
            val motorItems = listOf(
                MaintenanceItem(101, "Cambio de aceite", isChecked = false),
                MaintenanceItem(102, "Cambio filtro aceite", isChecked = true),
                MaintenanceItem(103, "Cambio filtro aire"),
                MaintenanceItem(104, "Cambio filtro de combustible"),
                MaintenanceItem(105, "Cambio de bujías"),
                MaintenanceItem(106, "Revisión líquido refrigerante")
            )

            val ruedasFrenosItems = listOf(
                MaintenanceItem(201, "Neumático delantero"),
                MaintenanceItem(202, "Neumático trasero"),
                MaintenanceItem(203, "Pastillas delanteras"),
                MaintenanceItem(204, "Pastillas traseras"),
                MaintenanceItem(205, "Cambio líquido frenos"),
                MaintenanceItem(206, "Presión neumáticos al guardar")
            )

            val transmisionItems = listOf(
                MaintenanceItem(301, "Engrase de cadena"),
                MaintenanceItem(302, "Tensión de cadena"),
                MaintenanceItem(303, "Cambio de kit de arrastre")
            )

            val electricoItems = listOf(
                MaintenanceItem(401, "Luces posición"),
                MaintenanceItem(402, "Luces carretera"),
                MaintenanceItem(403, "Luces largo alcance"),
                MaintenanceItem(404, "Luces intermitentes"),
                MaintenanceItem(405, "Batería"),
                MaintenanceItem(406, "Revisión fusibles"),
                MaintenanceItem(407, "Revisión cableado eléctrico")
            )

            val otrosItems = listOf(
                MaintenanceItem(501, "Acelerador"),
                MaintenanceItem(502, "Revisión Manetas"),
                MaintenanceItem(503, "Ajuste manetas de freno y embrague"),
                MaintenanceItem(504, "Revisión tornillería general"),
                MaintenanceItem(505, "Suspensión"),
                MaintenanceItem(506, "Preparación para invierno"),
                MaintenanceItem(507, "Revisión post-invierno"),
                MaintenanceItem(508, "Cuidado durante periodos de inactividad")
            )

            val initialSections = listOf(
                "Motor" to motorItems,
                "Ruedas y Frenos" to ruedasFrenosItems,
                "Transmisión" to transmisionItems,
                "Sistema Eléctrico" to electricoItems,
                "Otros" to otrosItems
            )
            _sections.value = initialSections
        }
    }

    fun onMaintenanceItemCheckedChange(itemId: Int, isChecked: Boolean) {
        val updatedSections = _sections.value.map { (title, items) ->
            title to items.map { item ->
                if (item.id == itemId) {
                    item.copy(isChecked = isChecked)
                } else {
                    item
                }
            }
        }
        _sections.value = updatedSections
    }

    fun saveMaintenanceState() {
        viewModelScope.launch {
            _sections.value.forEach { (_, items) ->
                items.forEach { item ->
                    println("Guardando estado de ${item.name} (ID: ${item.id}): ${item.isChecked}")
                }
            }
            println("Estado de mantenimiento guardado (simulado).")
        }
    }
}

class Mantenimiento : ComponentActivity() {

    private val maintenanceViewModel: MaintenanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TorqueTheme {
                MaintenanceColumn(maintenanceViewModel = maintenanceViewModel)
            }
        }
    }
}

@Composable
fun MaintenanceColumn(maintenanceViewModel: MaintenanceViewModel) {

    val secciones by maintenanceViewModel.sections.collectAsState()

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
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                secciones.forEach { (titulo, lista) ->
                    item {
                        Text(
                            text = titulo,
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(lista, key = { it.id }) { item ->
                        MantenimientoItemCard(
                            item = item,
                            onCheckedChange = { isChecked ->
                                maintenanceViewModel.onMaintenanceItemCheckedChange(item.id, isChecked)
                            }
                        )
                    }
                }
            }

            Button(
                onClick = { maintenanceViewModel.saveMaintenanceState() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Guardar Mantenimientos")
            }
        }
    }
}