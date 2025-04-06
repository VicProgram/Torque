package com.example.torque


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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.TorqueTheme

class PróximoMantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TorqueTheme {
                ProximoMantenimientoView()
            }
        }
    }
}


@Composable
fun ProximoMantenimientoView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Próximo Mantenimiento",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Aquí puedes mostrar la fecha de próximo mantenimiento
        Text(
            text = "Fecha: 10/05/2025", // Esta fecha es un ejemplo
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { /* Acción para agregar mantenimiento */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Agregar Mantenimiento")
        }
    }
}
