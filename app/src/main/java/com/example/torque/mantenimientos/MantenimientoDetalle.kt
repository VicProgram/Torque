package com.example.torque.mantenimientos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class MantenimientoDetalle(
    val idMoto: Int,
    val nombreMantenimiento: String,
    val km: Int,
    val precio: Double,
    val tiempo: String,
    val notas: String,
    val fecha: String
)

fun obtenerFechaActual(): String {
    val fecha = Calendar.getInstance().time
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formato.format(fecha)
}

@Composable
fun FormularioMantenimientoScreen(
    mantenimientosSeleccionados: List<String>,
    onGuardar: (List<MantenimientoDetalle>) -> Unit
) {
    var km by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalles del mantenimiento")

        OutlinedTextField(value = km, onValueChange = { km = it }, label = { Text("Kilómetros") })
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        OutlinedTextField(value = tiempo, onValueChange = { tiempo = it }, label = { Text("Tiempo de servicio") })
        OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val lista = mantenimientosSeleccionados.map { nombre ->
                MantenimientoDetalle(
                    idMoto = 1, // Cambia esto según tu lógica real
                    nombreMantenimiento = nombre,
                    km = km.toIntOrNull() ?: 0,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    tiempo = tiempo,
                    notas = notas,
                    fecha = obtenerFechaActual()
                )
            }
            onGuardar(lista)
        }) {
            Text("Guardar mantenimiento")
        }
    }
}
