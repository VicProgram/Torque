package com.example.torque.mantenimientos

import com.example.torque.ui.theme.TorqueTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.R
import com.example.torque.ui.theme.SquareButton


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
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.negrovertiama),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SquareButton(
                text = "➕\nAñadir nuevo\nmantenimiento",
                onClick = onNuevoMantenimiento
            )
            SquareButton(
                text = "📜\nVer historial",
                onClick = onVerHistorial
            )
        }
    }
}

