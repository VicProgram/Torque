package com.example.torque

import Moto
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.database.MiGarajeDatabaseHelper

class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idMotoString = intent.getStringExtra("idMoto")
        setContent {
            MotoDetalleScreen(idMotoString)
        }
    }
}

@Composable
fun MotoDetalleScreen(idMotoString: String?) {
    val context = LocalContext.current // Aquí obtenemos el contexto correctamente

    // Inicializamos el dbHelper dentro del contexto composable
    val dbHelper = remember { MiGarajeDatabaseHelper(context) }

    // Si el idMotoString no es nulo, intentamos obtener los detalles de la moto.
    val moto = remember(idMotoString) {
        idMotoString?.let {
            val idMoto = it.toIntOrNull()
            idMoto?.let { dbHelper.obtenerMotoPorId(it) }
        }
    }

    MotoDetalleView(moto)
}

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
                Text(text = "Color: ${moto.color_moto}", color = Color.White)
                Text(text = "Cilindrada: ${moto.cilindrada}", color = Color.White)
                Text(text = "CV: ${moto.cv}", color = Color.White)
                Text(text = "Estilo: ${moto.estilo}", color = Color.White)

                // Aquí puedes agregar otros detalles si lo deseas
                Button(
                    onClick = {
                        // Acción para cuando el botón sea presionado
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Volver", color = Color.White)
                }
            }
        }
    }
}
