package com.example.torque.garaje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.Moto
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

class AgregarMoto2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {

                AgregarMotoView()
            }
        }
    }
}
@Composable
fun AgregarMotoView() {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }

    // Estados de los campos, sin var delegado
    val marca = remember { mutableStateOf("") }
    val modelo = remember { mutableStateOf("") }
    val anno = remember { mutableStateOf("") }
    val matricula = remember { mutableStateOf("") }
    val colorMoto = remember { mutableStateOf("") }
    val cilindrada = remember { mutableStateOf("") }
    val cv = remember { mutableStateOf("") }
    val estilo = remember { mutableStateOf("") }
    val kms = remember { mutableStateOf("") }
    val fechaCompra = remember { mutableStateOf("") }

    // Estado para manejar la URI de la imagen seleccionada
    val imageUri = remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Añadir Moto")

        OutlinedTextField(value = marca.value, onValueChange = { marca.value = it }, label = { Text("Marca") })
        OutlinedTextField(value = modelo.value, onValueChange = { modelo.value = it }, label = { Text("Modelo") })
        OutlinedTextField(value = anno.value, onValueChange = { anno.value = it }, label = { Text("Año") })
        OutlinedTextField(value = matricula.value, onValueChange = { matricula.value = it }, label = { Text("Matrícula") })
        OutlinedTextField(value = colorMoto.value, onValueChange = { colorMoto.value = it }, label = { Text("Color") })
        OutlinedTextField(value = cilindrada.value, onValueChange = { cilindrada.value = it }, label = { Text("Cilindrada") })
        OutlinedTextField(value = cv.value, onValueChange = { cv.value = it }, label = { Text("CV") })
        OutlinedTextField(value = estilo.value, onValueChange = { estilo.value = it }, label = { Text("Estilo") })
        OutlinedTextField(value = kms.value, onValueChange = { kms.value = it }, label = { Text("Kms") })
        OutlinedTextField(value = fechaCompra.value, onValueChange = { fechaCompra.value = it }, label = { Text("Fecha Compra") })

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para seleccionar una imagen
        Button(onClick = {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as Activity).startActivityForResult(intent, 1001) // Verifica el requestCode
        }) {
            Text("Seleccionar Foto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para guardar la moto
        Button(onClick = {
            val moto = Moto(
                idMoto = "0",
                marca = marca.value,
                modelo = modelo.value,
                anno = anno.value.toIntOrNull() ?: 0,
                matricula = matricula.value,
                color_moto = colorMoto.value,
                cilindrada = cilindrada.value,
                cv = cv.value.toIntOrNull() ?: 0,
                estilo = estilo.value,
                kms = kms.value.toIntOrNull() ?: 0,
                fecha_compra = fechaCompra.value,
                esPrincipal = 0,
                // Guardamos la URI de la imagen como una ruta en la base de datos
                foto_moto = imageUri.value ?: ""
            )

            // Inserción en la base de datos
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
    }
}
