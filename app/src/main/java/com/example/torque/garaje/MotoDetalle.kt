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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            val ruta = copiarImagenEnCache(context, it)
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
        Image(
            painter = painterResource(id = R.drawable.negroblue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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
                Text("No se ha encontrado la moto.", color = Color.White)
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
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
                        // Se pasa el idMoto directamente como Int
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
                        // Se pasa el idMoto directamente como Int
                        val success = dbHelper.eliminarMoto(moto.idMoto.toString())
                        if (success) {
                            Toast.makeText(
                                context, "Moto eliminada correctamente", Toast.LENGTH_SHORT
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
                        onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()
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

// Copia la imagen en cache local para su uso interno
fun copiarImagenEnCache(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "imagen_moto_${System.currentTimeMillis()}.jpg"

        val outputFile = File(context.cacheDir, fileName)
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
