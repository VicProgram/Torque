package com.example.torque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import com.example.torque.configuracion.Configuracion
import com.example.torque.database.copiarBaseDeDatos
import com.example.torque.garaje.ListaGaraje
import com.example.torque.mantenimientos.MantenimientoPreview
import com.example.torque.ui.theme.BotonLargo
import com.example.torque.ui.theme.TorqueTheme
import com.example.torque.ui.theme.pepperoni

class MenuPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        copiarBaseDeDatos(this)

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
            painter = painterResource(id = R.drawable.negroama),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Torqu\uE005", style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = pepperoni,
                    color = Color.White,
                    fontSize = 90.dp.value.sp,
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black, blurRadius = 2f
                    )
                ),
                modifier = Modifier.padding(top = 35.dp, bottom = 5.dp)
            )

            BotonLargo(
                onClick = {
                    val intent = Intent(context, ListaGaraje::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                texto = "Mi Garaje"
            )

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

            BotonLargo(
                onClick = {
                    val intent = Intent(context, MantenimientoPreview::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                texto = "Mantenimientos"
            )
        }
    }
}
