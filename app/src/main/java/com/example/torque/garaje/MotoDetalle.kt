package com.example.torque.garaje

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.R
import com.example.torque.database.TorqueDatabaseHelper


class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idMoto = intent.getIntExtra("idMoto", -1)
        val dbHelper = TorqueDatabaseHelper(this)
        val moto = if (idMoto != -1) dbHelper.obtenerMotoPorId(idMoto) else null

        setContent {
            MaterialTheme {
                MotoDetalleView(
                    moto = moto, dbHelper = dbHelper, context = this, activity = this
                )
            }
        }
    }
}

@Composable
fun MotoDetalleView(
    moto: Moto?, dbHelper: TorqueDatabaseHelper, context: Context, activity: Activity
) {
    val fotos = remember { mutableStateListOf<String>() }

    // Cargar fotos desde la base de datos
    LaunchedEffect(moto?.idMoto) {
        moto?.idMoto?.let {
            fotos.clear()
            fotos.addAll(dbHelper.obtenerFotosDeMoto(it))
        }
    }

    // Launcher para seleccionar foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val ruta = dbHelper.copiarImagenEnCache(context, it)
            if (ruta != null && moto != null) {
                dbHelper.insertarFoto(moto.idMoto, ruta)
                fotos.add(ruta)
                Toast.makeText(context, "Foto añadida", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al añadir foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con el drawable 'negrolinama'
        Image(
            painter = painterResource(id = R.drawable.negrolinama), // Aquí se usa el fondo
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize() // Aseguramos que ocupe toda la pantalla
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)) // Fondo oscuro para los detalles
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (moto == null) {
                Text("No se ha encontrado la moto.", color = Color.White)
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Marca y modelo al inicio como título
                    Text(
                        text = moto.marca,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = moto.modelo,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(16.dp)) // Espacio entre el título y los detalles

                    // Título "Detalles de la moto"
                    Text(
                        text = "Detalles de la moto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))

                    // Detalles de la moto
                    Text("Marca: ${moto.marca}", color = Color.White)
                    Text("Modelo: ${moto.modelo}", color = Color.White)
                    Text("Año: ${moto.anno}", color = Color.White)
                    Text("Matrícula: ${moto.matricula}", color = Color.White)
                    Text("Color: ${moto.colorMoto}", color = Color.White)
                    Text("Cilindrada: ${moto.cilindrada}", color = Color.White)
                    Text("CV: ${moto.cv}", color = Color.White)
                    Text("Estilo: ${moto.estilo}", color = Color.White)

                    Spacer(Modifier.height(24.dp))

                    // Botones de acción
                    Button(onClick = {
                        val success = dbHelper.hacerPrincipal(moto.idMoto.toString())
                        Toast.makeText(
                            context,
                            if (success) "Moto establecida como principal" else "Error al establecer como principal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text("Establecer como principal")
                    }

                    Button(onClick = {
                        val success = dbHelper.eliminarMoto(moto.idMoto.toString())
                        if (success) {
                            Toast.makeText(
                                context,
                                "Moto eliminada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        } else {
                            Toast.makeText(context, "Error al eliminar la moto", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Eliminar Moto")
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(onClick = { activity.finish() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Volver")
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir Foto")
                    }

                    Spacer(Modifier.height(16.dp))

                    if (fotos.isNotEmpty()) {
                        Text("Fotos", color = Color.White)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(fotos) { ruta ->
                                Image(
                                    painter = rememberAsyncImagePainter(ruta),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .background(Color.DarkGray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
