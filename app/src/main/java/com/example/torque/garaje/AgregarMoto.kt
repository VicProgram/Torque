package com.example.torque.garaje

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.R
import com.example.torque.components.BackgroundImage
import com.example.torque.components.SectionHeader
import com.example.torque.components.TorqueButton
import com.example.torque.components.TorqueTextField
import com.example.torque.components.showToast
import com.example.torque.database.TorqueDatabaseHelper

class AgregarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AgregarMotoView() }
    }
}

@Composable
fun AgregarMotoView() {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }
    val scrollState = rememberScrollState()

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

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(R.drawable.negrobluevert, alpha = 0.95f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionHeader("Nueva Moto")

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

            Spacer(Modifier.height(16.dp))

            TorqueButton("Seleccionar Foto", { imagePicker.launch("image/*") }, color = Color.Gray)

            imageUri?.let {
                Spacer(Modifier.height(12.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier.height(180.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(24.dp))

            TorqueButton("Guardar Moto", {
                val moto = Moto(0, marca, modelo, anno.toIntOrNull() ?: 0, matricula, colorMoto, cilindrada, cv, estilo, kms.toIntOrNull() ?: 0, fechaCompra, 0, imageUri?.toString() ?: "")
                if (dbHelper.agregarMoto(moto)) {
                    showToast(context, "¡Guardada!")
                    (context as Activity).apply { setResult(Activity.RESULT_OK); finish() }
                } else {
                    showToast(context, "Error al guardar")
                }
            })
            Spacer(Modifier.height(40.dp))
        }
    }
}
