package com.example.torque

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.TorqueTheme

class Configuracion : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TorqueTheme {
                ConfiguracionView()
            }
        }
    }
}

@Composable
fun ConfiguracionView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { /* Cambiar idioma */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Idioma")
        }

        Button(
            onClick = { /* Cambiar Tema Oscuro/Claro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Tema Oscuro/Claro")
        }
    }
}
