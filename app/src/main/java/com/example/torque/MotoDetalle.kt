package com.example.torque


import Moto
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.torque.database.MiGarajeDatabaseHelper

class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID de la moto como String
        val idMotoString = intent.getStringExtra("idMoto")

        if (idMotoString != null) {
            // Convertir el String a Int
            val idMoto = idMotoString.toIntOrNull() // Usamos toIntOrNull() para evitar excepciones si no es un número válido

            if (idMoto != null) {
                val dbHelper = MiGarajeDatabaseHelper(this)  // Crear la instancia de MiGarajeDatabaseHelper
                val moto = dbHelper.obtenerMotoPorId(idMoto)  // Llamar al método con el ID convertido

                setContent {
                    MotoDetalleView(moto)
                }
            } else {
                setContent {
                    MotoDetalleView(null) // Si no se pudo convertir el ID, mostrar mensaje de error
                }
            }
        } else {
            setContent {
                MotoDetalleView(null) // Si no se recibe el ID, mostrar mensaje de error
            }
        }
    }
}

// MotoDetalle.kt
@Composable
fun MotoDetalleView(moto: Moto?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (moto == null) {
            Text(
                text = "No se ha encontrado la moto.",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Detalles de la Moto",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(text = "Marca: ${moto.marca}", color = Color.White)
                Text(text = "Modelo: ${moto.modelo}", color = Color.White)
                Text(text = "Año: ${moto.anno}", color = Color.White)
                Text(text = "Matricula: ${moto.matricula}", color = Color.White)
                Text(text = "Color: ${moto.color_moto}", color = Color.Gray)
                Text(text = "Cilindrada: ${moto.cilindrada}", color = Color.White)
                Text(text = "CV: ${moto.cv}", color = Color.White)
                Text(text = "Estilo: ${moto.estilo}", color = Color.White)
                Text(text = "Kms: ${moto.kms}", color = Color.White)
                Text(text = "Fecha de compra: ${moto.fecha_compra}", color = Color.White)

            }
        }
    }
}
