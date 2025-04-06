package com.example.torque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.torque.ui.theme.TorqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MainMenu()  // Llamamos al composable que no necesita parámetros
            }
        }
    }
}

@Composable
fun MainMenu() {
    // Obtener el contexto necesario para iniciar actividades
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Torque",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón para navegar a Revisiones
        Button(
            onClick = {
                val intent = Intent(context, Revisiones::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Revisiones")
        }

        // Botón para navegar a Configuración
        Button(
            onClick = {
                val intent = Intent(context, Configuracion::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Configuración")
        }

        // Botón para navegar a Próximo Mantenimiento
        Button(
            onClick = {
                val intent = Intent(context, PróximoMantenimiento::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Próximo Mantenimiento")
        }
    }
}
