package com.example.torque.mantenimientos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.torque.database.TorqueDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MantenimientoFormulario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seleccionados = intent.getStringArrayListExtra("seleccionados") ?: arrayListOf()

        setContent {
            MantenimientoFormularioScreen(seleccionados)
        }
    }
}

@Composable
fun MantenimientoFormularioScreen(seleccionados: List<String>) {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }

    var marca by remember { mutableStateOf(TextFieldValue("")) }
    var modelo by remember { mutableStateOf(TextFieldValue("")) }
    var anno by remember { mutableStateOf(TextFieldValue("")) }
    var km by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var tiempo by remember { mutableStateOf(TextFieldValue("")) }
    var notas by remember { mutableStateOf(TextFieldValue("")) }

    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black.copy(alpha = 0.1f)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Formulario de Mantenimiento", style = MaterialTheme.typography.headlineSmall)

        CustomTextField("Marca", marca) { marca = it }
        CustomTextField("Modelo", modelo) { modelo = it }
        CustomTextField("Año", anno) { anno = it }
        CustomTextField("Kilómetros", km) { km = it }
        CustomTextField("Precio", precio) { precio = it }
        CustomTextField("Tiempo invertido", tiempo) { tiempo = it }
        CustomTextField("Notas", notas) { notas = it }

        Button(
            onClick = {
                val mantenimiento = MantenimientoDetalle(
                    nombreMantenimiento = "Mantenimiento",
                    date = fechaActual,
                    kilometers = km.text.toIntOrNull(),
                    marca = marca.text,
                    modelo = modelo.text,
                    anno = anno.text.toIntOrNull(),
                    precio = precio.text,
                    tiempo = tiempo.text,
                    notas = notas.text,
                    tareas = seleccionados.joinToString(", ")
                )

                dbHelper.insertarMantenimiento(mantenimiento)
                Toast.makeText(context, "Mantenimiento guardado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mantenimiento")
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.small)
                .padding(10.dp)
        )
    }
}
