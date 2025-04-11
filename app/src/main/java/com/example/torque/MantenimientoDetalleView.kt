package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable // Importamos clickable aquí
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

// Definimos los campos que se deben capturar para cada tipo de mantenimiento
data class MantenimientoDetalleCampos(
    val nombre: String,
    val campos: List<MantenimientoCampo>
)

sealed class MantenimientoCampo {
    data class Texto(val label: String, val value: String) : MantenimientoCampo()
    data class Selector(val label: String, val opciones: List<String>, val selected: String) : MantenimientoCampo()
    data class Booleano(val label: String, val checked: Boolean) : MantenimientoCampo()
}

class MantenimientoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el nombre del tipo de mantenimiento
        val tipoMantenimiento = intent.getStringExtra("titulo") ?: "Detalle Mantenimiento"

        // Simular la obtención de campos según el tipo de mantenimiento
        val campos = obtenerCamposPorTipo(tipoMantenimiento)

        setContent {
            MantenimientoFormulario(tipoMantenimiento, campos)
        }
    }

    // Aquí definimos los campos que deben ser presentados según el tipo de mantenimiento
    private fun obtenerCamposPorTipo(tipo: String): MantenimientoDetalleCampos {
        return when (tipo) {
            "Cambio de aceite" -> MantenimientoDetalleCampos(
                nombre = tipo,
                campos = listOf(
                    MantenimientoCampo.Texto("Marca de Aceite", ""),
                    MantenimientoCampo.Texto("Viscosidad", ""),
                    MantenimientoCampo.Texto("Litros", ""),
                    MantenimientoCampo.Booleano("¿Cambio de Filtro?", false)
                )
            )
            "Neumáticos" -> MantenimientoDetalleCampos(
                nombre = tipo,
                campos = listOf(
                    MantenimientoCampo.Selector("Posición del neumático", listOf("Delantero", "Trasero"), "Delantero"),
                    MantenimientoCampo.Texto("Marca", ""),
                    MantenimientoCampo.Texto("Modelo", ""),
                    MantenimientoCampo.Texto("Medidas", "")
                )
            )
            else -> MantenimientoDetalleCampos(
                nombre = tipo,
                campos = listOf(
                    MantenimientoCampo.Texto("Información", "")
                )
            )
        }
    }
}

@Composable
fun MantenimientoFormulario(tipo: String, detalleCampos: MantenimientoDetalleCampos) {
    var camposState = remember { mutableStateListOf(*detalleCampos.campos.toTypedArray()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Asegurándonos de pasar un valor válido a `text`
        Text(text = "Detalles de: ${detalleCampos.nombre}", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        // Mostrar los campos según el tipo de mantenimiento
        camposState.forEachIndexed { index, campo ->
            when (campo) {
                is MantenimientoCampo.Texto -> {
                    var textState by remember { mutableStateOf(campo.value) }

                    OutlinedTextField(
                        value = textState,
                        onValueChange = { textState = it },
                        label = { Text(campo.label) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions.Default
                    )
                }
                is MantenimientoCampo.Selector -> {
                    var selectedOption by remember { mutableStateOf(campo.selected) }
                    var expanded by remember { mutableStateOf(false) }

                    // Botón para mostrar el dropdown
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
                            onValueChange = { },
                            label = { Text(campo.label) },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth().clickable {
                                expanded = !expanded
                            }
                        )

                        // Mostrar el menú desplegable
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            campo.opciones.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                    }
                                ) {
                                    Text(text = option) // Aquí pasamos el texto correctamente
                                }
                            }
                        }
                    }
                }
                is MantenimientoCampo.Booleano -> {
                    var checkedState by remember { mutableStateOf(campo.checked) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = checkedState, onCheckedChange = { checkedState = it })
                        Text(campo.label) // Aseguramos de pasar el texto de la etiqueta
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón para guardar los detalles
        Button(
            onClick = {
                // Aquí puedes implementar la lógica para guardar los datos
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Guardar")
        }
    }
}
