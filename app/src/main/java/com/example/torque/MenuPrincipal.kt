package com.example.torque

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.torque.ui.componentes.BotonLargo
import com.example.torque.ui.theme.TorqueTheme


class MenuPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MenuPrincipalView()  // Llamamos al composable que no necesita parámetros
            }
        }
    }
}

@Composable
fun MenuPrincipalView() {
    // Obtener el contexto necesario para iniciar actividades
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

        // Botón para navegar a Revisiones
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

