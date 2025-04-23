package com.example.torque.garaje

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
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
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }

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
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.negroblue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.03f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Añadir Moto", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") })
            OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
            OutlinedTextField(value = anno, onValueChange = { anno = it }, label = { Text("Año") })
            OutlinedTextField(value = matricula, onValueChange = { matricula = it }, label = { Text("Matrícula") })
            OutlinedTextField(value = colorMoto, onValueChange = { colorMoto = it }, label = { Text("Color") })
            OutlinedTextField(value = cilindrada, onValueChange = { cilindrada = it }, label = { Text("Cilindrada") })
            OutlinedTextField(value = cv, onValueChange = { cv = it }, label = { Text("CV") })
            OutlinedTextField(value = estilo, onValueChange = { estilo = it }, label = { Text("Estilo") })
            OutlinedTextField(value = kms, onValueChange = { kms = it }, label = { Text("Kms") })
            OutlinedTextField(value = fechaCompra, onValueChange = { fechaCompra = it }, label = { Text("Fecha Compra") })

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                imagePickerLauncher.launch("image/*")
            }) {
                Text("Seleccionar Foto")
            }

            imageUri?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Foto Moto",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                val moto = Moto(
                    idMoto = 0,
                    marca = marca,
                    modelo = modelo,
                    anno = anno.toIntOrNull() ?: 0,
                    matricula = matricula,
                    colorMoto = colorMoto,
                    cilindrada = cilindrada,
                    cv = cv.toIntOrNull() ?: 0,
                    estilo = estilo,
                    kms = kms.toIntOrNull() ?: 0,
                    fechaCompra = fechaCompra,
                    esPrincipal = 0,
                    fotoMoto = imageUri?.toString() ?: ""
                )
                val success = dbHelper.agregarMoto(moto)
                if (success) {
                    Toast.makeText(context, "Moto guardada correctamente", Toast.LENGTH_SHORT).show()
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

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
