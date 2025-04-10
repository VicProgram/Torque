package com.example.torque

import MiGarajeDatabaseHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.ui.componentes.BotonLargo
import com.example.torque.ui.theme.TorqueTheme



class MenuPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Copiar la base de datos desde assets si no existe
        //copiarBaseDeDatos(applicationContext)
        probarBaseDeDatos(applicationContext)  // <--- Aquí llamamos a la prueba

        setContent {
            TorqueTheme {
                MenuPrincipalView()
            }
        }
    }
}

@Composable
fun MenuPrincipalView() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondoappwebp),
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Torque",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 35.dp, bottom = 5.dp)
        )

        // Botón para navegar a Mi Garaje
        BotonLargo(
            onClick = {
                val intent = Intent(context, MiGaraje::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            texto = "Mi Garaje"
        )

        // Botón para navegar a Configuración
        BotonLargo(
            onClick = {
                val intent = Intent(context, Configuracion::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            texto = "Configuración"
        )

        // Botón para navegar a Próximo Mantenimiento
        BotonLargo(
            onClick = {
                val intent = Intent(context, Mantenimiento::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            texto = "Mantenimientos"
        )
    }
}

fun probarBaseDeDatos(context: Context) {
    val dbHelper = MiGarajeDatabaseHelper(context)
    val db = dbHelper.readableDatabase

    val cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
    while (cursor1.moveToNext()) {
        val tableName = cursor1.getString(0)
        Log.d("BD_TABLAS", "Tabla encontrada: $tableName")
    }
    cursor1.close()

    val cursor2 = db.rawQuery("SELECT * FROM MiGaraje", null)

    if (cursor2.moveToFirst()) {
        do {
            val marca = cursor2.getString(cursor2.getColumnIndexOrThrow("Marca"))
            val modelo = cursor2.getString(cursor2.getColumnIndexOrThrow("Modelo"))
            Log.d("BD_PRUEBA", "Moto: $marca $modelo")
        } while (cursor2.moveToNext())
    } else {
        Log.d("BD_PRUEBA", "No hay motos en la base de datos.")
    }

    cursor2.close()
    db.close()
}
