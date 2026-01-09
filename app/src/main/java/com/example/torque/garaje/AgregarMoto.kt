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
import com.example.torque.components.SectionHeader
import com.example.torque.components.TorqueTextField
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
        Image(
            painter = painterResource(id = R.drawable.negrobluevert),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionHeader("Añadir Moto")

            TorqueTextField(marca, { marca = it }, "Marca")
            TorqueTextField(modelo, { modelo = it }, "Modelo")
            TorqueTextField(anno, { anno = it }, "Año")
            TorqueTextField(matricula, { matricula = it }, "Matrícula")
            TorqueTextField(colorMoto, { colorMoto = it }, "Color")
            TorqueTextField(cilindrada, { cilindrada = it }, "Cilindrada")
            TorqueTextField(cv, { cv = it }, "CV")
            TorqueTextField(estilo, { estilo = it }, "Estilo")
            TorqueTextField(kms, { kms = it }, "Kms")
            TorqueTextField(fechaCompra, { fechaCompra = it }, "Fecha Compra")

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
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
                    cv = (cv.toIntOrNull() ?: 0).toString(),
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
