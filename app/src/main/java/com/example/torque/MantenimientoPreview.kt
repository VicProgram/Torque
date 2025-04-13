package com.example.torque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.TorqueTheme

class MantenimientoPreview: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                SeleccionMantenimientoScreen(
                    onNuevoMantenimiento = {
                        startActivity(Intent(this, Mantenimiento::class.java))
                    },
                    onVerHistorial = {
                        startActivity(Intent(this, MantenimientoHistorial::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun SeleccionMantenimientoScreen(
    onNuevoMantenimiento: () -> Unit,
    onVerHistorial: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onNuevoMantenimiento) {
                Text(text = "➕ Añadir nuevo mantenimiento")
            }

            Button(onClick = onVerHistorial) {
                Text(text = "📜 Ver historial")
            }
        }
    }
}
