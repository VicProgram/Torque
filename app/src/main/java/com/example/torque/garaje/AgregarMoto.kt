package com.example.torque.garaje

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.Moto
import com.example.torque.R
import com.example.torque.database.TorqueDatabaseHelper

class AgregarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgregarMotoView()
        }
    }
}

@Composable
fun AgregarMotoView() {

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.negroblue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura encima de la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.03f))
        )

        // Contexto y helper declarados al inicio del Composable
        val context = LocalContext.current
        val dbHelper = remember { TorqueDatabaseHelper(context) }

        // Estados de los campos
        var marca by remember { mutableStateOf("") }
        var modelo by remember { mutableStateOf("") }
        var anno by remember { mutableStateOf("") }
        var matricula by remember { mutableStateOf("") }
        var colorMoto by remember { mutableStateOf("") }
        var cilindrada by remember { mutableStateOf("") }
        var cv by remember { mutableStateOf("") }
        var estilo by remember { mutableStateOf("") }
        var kms by remember { mutableStateOf("") }
        var fechaCompra by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Añadir Moto")

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") })
            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo") })
            OutlinedTextField(value = anno, onValueChange = { anno = it }, label = { Text("Año") })
            OutlinedTextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = { Text("Matrícula") })
            OutlinedTextField(
                value = colorMoto,
                onValueChange = { colorMoto = it },
                label = { Text("Color") })
            OutlinedTextField(
                value = cilindrada,
                onValueChange = { cilindrada = it },
                label = { Text("Cilindrada") })
            OutlinedTextField(value = cv, onValueChange = { cv = it }, label = { Text("CV") })
            OutlinedTextField(
                value = estilo,
                onValueChange = { estilo = it },
                label = { Text("Estilo") })
            OutlinedTextField(value = kms, onValueChange = { kms = it }, label = { Text("Kms") })
            OutlinedTextField(
                value = fechaCompra,
                onValueChange = { fechaCompra = it },
                label = { Text("Fecha Compra") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Validaciones y creación de la moto
                val moto = Moto(
                    idMoto = "0",
                    marca = marca,
                    modelo = modelo,
                    anno = anno.toIntOrNull() ?: 0,
                    matricula = matricula,
                    color_moto = colorMoto,
                    cilindrada = cilindrada,
                    cv = cv.toIntOrNull() ?: 0,
                    estilo = estilo,
                    kms = kms.toIntOrNull() ?: 0,
                    fecha_compra = fechaCompra,
                    esPrincipal = 0,
                    foto_moto = ""
                )

                // Inserción en la base de datos
                val success = dbHelper.agregarMoto(moto)
                if (success) {
                    Toast.makeText(context, "Moto guardada correctamente", Toast.LENGTH_SHORT)
                        .show()
                    // Devolver RESULT_OK y cerrar la Activity
                    (context as Activity).apply {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    Toast.makeText(context, "Error al guardar la moto", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Guardar Moto")
            }
        }
    }
}
