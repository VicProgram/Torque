package com.example.torque.maintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.DashboardBottomNav
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.ui.theme.TorqueTheme

class MaintenancePreview : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                SeleccionMantenimientoScreen(
                    onNuevoMantenimiento = {
                        // Abrir MaintenanceLog para seleccionar tareas
                        startActivity(Intent(this, MaintenanceLog::class.java))
                    },
                    onVerHistorial = {
                        // startActivity(Intent(this, MantenimientoHistorial::class.java))
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
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Header ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }
                Text(
                    text = "Mantenimiento",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { /* Settings */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MantenimientoOptionCard(
                    title = "Nuevo Registro",
                    subtitle = "Registrar tareas realizadas",
                    icon = Icons.Default.Build,
                    color = TorqueColors.Primary,
                    onClick = onNuevoMantenimiento
                )

                Spacer(modifier = Modifier.height(24.dp))

                MantenimientoOptionCard(
                    title = "Ver Historial",
                    subtitle = "Consulta registros pasados",
                    icon = Icons.Default.History,
                    color = TorqueColors.Accent,
                    onClick = onVerHistorial
                )
            }
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun MantenimientoOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(TorqueColors.SurfaceDark)
            .clickable(onClick = onClick)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
        }
        Column {
            Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = TorqueColors.OnSurfaceVariant, fontSize = 14.sp)
        }
    }
}
