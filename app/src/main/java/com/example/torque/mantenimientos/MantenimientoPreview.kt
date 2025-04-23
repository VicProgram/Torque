package com.example.torque.mantenimientos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.torque.ui.theme.SquareButton
import com.example.torque.ui.theme.TorqueTheme


class MantenimientoPreview : ComponentActivity() {
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
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center // Centra todo el contenido (el Row)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Añadir
            SquareButton(
                text = "➕\nAñadir nuevo\nmantenimiento",
                onClick = onNuevoMantenimiento
            )

            // Botón Ver Historial
            SquareButton(
                text = "📜\nVer historial",
                onClick = onVerHistorial
            )
        }
    }
}