package com.example.torque.maintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import com.example.torque.garage.Moto

class MaintenancePreview : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = TorqueDatabaseHelper(this)
        var motoId = intent.getLongExtra("motoId", -1L)

        // Si no viene motoId, intentar buscar la principal
        if (motoId == -1L) {
            motoId = dbHelper.getPrincipalMoto()?.idMoto ?: -1L
        }

        setContent {
            TorqueTheme {
                SeleccionMantenimientoScreen(
                    motoId = motoId,
                    onNuevoMantenimiento = {
                        val intent = Intent(this, MaintenanceLog::class.java).apply {
                            putExtra("motoId", motoId)
                        }
                        startActivity(intent)
                    },
                    onVerHistorial = {
                        val intent = Intent(this, MantenimientoHistorial::class.java).apply {
                            putExtra("motoId", motoId)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun SeleccionMantenimientoScreen(
    motoId: Long,
    onNuevoMantenimiento: () -> Unit,
    onVerHistorial: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }
    val moto = remember(motoId) { if (motoId != -1L) dbHelper.getMotoById(motoId) else null }

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
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Mantenimiento",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info de la moto seleccionada
                moto?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TorqueColors.SurfaceDark),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Motorcycle, contentDescription = null, tint = TorqueColors.Primary)
                            Column {
                                Text("${it.marca} ${it.modelo}", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("${it.kms} km", color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp)
                            }
                        }
                    }
                }

                MantenimientoOptionCard(
                    title = "Nuevo Registro",
                    subtitle = "Registrar tareas realizadas",
                    icon = Icons.Default.Build,
                    color = TorqueColors.Primary,
                    onClick = onNuevoMantenimiento
                )

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
