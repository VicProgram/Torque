package com.example.torque.garaje

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.R
import com.example.torque.database.TorqueDatabaseHelper
import java.io.File

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
    var selectedPhoto by remember { mutableStateOf<String?>(null) }

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
            val ruta = copiarImagenPermanente(context, it)
            if (ruta != null && moto != null) {
                dbHelper.insertarFoto(moto.idMoto, ruta)
                fotos.add(ruta)
                Toast.makeText(context, "Foto añadida", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al añadir foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.negrolinama),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(16.dp)
        ) {
            if (moto == null) {
                Text(
                    text = "No se ha encontrado la moto.",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {

                    Text(
                        text = "Detalles de la Moto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.02f))
                            .padding(8.dp)
                    ) {
                        Column { // Un Column dentro del Box para organizar los detalles
                            val detalles = listOf(
                                "Marca" to moto.marca,
                                "Modelo" to moto.modelo,
                                "Año" to moto.anno,
                                "Matrícula" to moto.matricula,
                                "Color" to moto.colorMoto,
                                "Cilindrada" to moto.cilindrada,
                                "CV" to moto.cv,
                                "Estilo" to moto.estilo
                            )
                            detalles.forEach { (etiqueta, valor) ->
                                Text(
                                    text = "$etiqueta: $valor",
                                    color = Color.White, // Cambia el color del texto para que contraste con el fondo
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val success = dbHelper.hacerPrincipal(moto.idMoto.toString())
                            Toast.makeText(
                                context,
                                if (success) "Moto establecida como principal" else "Error al establecer como principal",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Establecer como principal")
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val success = dbHelper.eliminarMoto(moto.idMoto.toString())
                            if (success) {
                                Toast.makeText(
                                    context, "Moto eliminada correctamente", Toast.LENGTH_SHORT
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
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir Foto")
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { activity.finish() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }

                    Spacer(Modifier.height(16.dp))

                    if (fotos.isNotEmpty()) {
                        Text("Fotos", color = Color.White)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(fotos) { ruta ->
                                var showDeleteButton by remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .background(Color.DarkGray)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    selectedPhoto = ruta
                                                    showDeleteButton = true
                                                })
                                        }) {
                                    Image(
                                        painter = rememberAsyncImagePainter(ruta),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    if (showDeleteButton) {
                                        Button(
                                            onClick = {
                                                dbHelper.eliminarFoto(moto.idMoto, ruta)
                                                fotos.remove(ruta)
                                                showDeleteButton = false
                                                Toast.makeText(
                                                    context, "Foto eliminada", Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                        ) {
                                            Text("Eliminar", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun copiarImagenPermanente(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "imagen_moto_${System.currentTimeMillis()}.jpg"

        val outputFile = File(context.filesDir, fileName)
        inputStream?.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        outputFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
