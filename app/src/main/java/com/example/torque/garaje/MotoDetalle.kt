package com.example.torque.garaje

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.R
import com.example.torque.database.TorqueDatabaseHelper

class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el id de la moto desde el intent
        val idMoto = intent.getStringExtra("idMoto")?.toIntOrNull()
        val dbHelper = TorqueDatabaseHelper(this)
        val moto = idMoto?.let { dbHelper.obtenerMotoPorId(it) }

        setContent {
            MaterialTheme {
                MotoDetalleView(
                    moto = moto, // Aseguramos que 'moto' es un objeto válido
                    dbHelper = dbHelper,
                    context = this,
                    activity = this
                )
            }
        }
    }
}

@Composable
fun MotoDetalleView(
    moto: Moto?, // Cambio el nombre de 'Moto' a 'moto' (minúscula)
    dbHelper: TorqueDatabaseHelper,
    context: Context,
    activity: Activity
) {
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            if (moto == null) {
                Text(
                    text = "No se ha encontrado la moto.",
                    color = Color.White
                )
            } else {
                Column {
                    Text(
                        "Detalles de la Moto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Marca: ${moto.marca}", color = Color.White)
                    Text("Modelo: ${moto.modelo}", color = Color.White)
                    Text("Año: ${moto.anno}", color = Color.White)
                    Text("Matrícula: ${moto.matricula}", color = Color.White)
                    Text("Color: ${moto.colorMoto}", color = Color.White)
                    Text("Cilindrada: ${moto.cilindrada}", color = Color.White)
                    Text("CV: ${moto.cv}", color = Color.White)
                    Text("Estilo: ${moto.estilo}", color = Color.White)

                    Spacer(Modifier.height(24.dp))

                    Button(onClick = {
                        val success = dbHelper.hacerPrincipal(moto.idMoto)
                        if (success) {
                            Toast.makeText(context, "Moto establecida como principal", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al establecer como principal", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Establecer como principal")
                    }


                    Button(
                        onClick = {
                            val success = dbHelper.eliminarMoto(moto.idMoto)
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Moto eliminada correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                activity.setResult(Activity.RESULT_OK)
                                activity.finish()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al eliminar la moto",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar Moto")
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { activity.finish() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
