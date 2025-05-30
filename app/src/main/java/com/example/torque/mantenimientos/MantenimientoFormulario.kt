package com.example.torque.mantenimientos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import java.text.SimpleDateFormat
import java.util.*



class MantenimientoFormulario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MantenimientoFormularioScreen(onGuardar = { mantenimiento ->
                        val dbHelper = TorqueDatabaseHelper(this)
                        dbHelper.insertarMantenimiento(mantenimiento)
                        finish()
                    })
                }
            }
        }
    }
}

@Composable
fun MantenimientoFormularioScreen(
    onGuardar: (MantenimientoDetalle) -> Unit
) {
    var nombreMantenimiento by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    val fecha = obtenerFechaActual()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Formulario de Mantenimiento", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreMantenimiento,
            onValueChange = { nombreMantenimiento = it },
            label = { Text("Nombre del mantenimiento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = marca,
            onValueChange = { marca = it },
            label = { Text("Marca") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = modelo,
            onValueChange = { modelo = it },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombreMantenimiento.isNotBlank()) {
                    val mantenimiento = MantenimientoDetalle(
                        marca = marca,
                        modelo = modelo,
                        precio = precio,
                        date = fecha,
                        kilometers = null,
                        mantenimientoId = 0,
                        nombreMantenimiento = nombreMantenimiento
                    )
                    onGuardar(mantenimiento)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

fun obtenerFechaActual(): String {
    val fecha = Calendar.getInstance().time
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formato.format(fecha)
}
