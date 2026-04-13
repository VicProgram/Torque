package com.example.torque.maintenance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetalleMantenimientoScreen(
    mantenimiento: MaintenanceDetailData,
    kmsActualesMoto: Int,
    onBack: () -> Unit
) {
    val kmsRestantes = mantenimiento.kmsRestantes(kmsActualesMoto)
    val progreso = mantenimiento.progreso(kmsActualesMoto)
    val vencido = mantenimiento.estaVencido(kmsActualesMoto)

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Detalles del Mantenimiento", style = MaterialTheme.typography.headlineSmall)

            // ── Cuenta atrás ──────────────────────────────────────
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (vencido) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (vencido) "⚠️ VENCIDO por ${-kmsRestantes} km" else "✅ Próximo en $kmsRestantes km",
                        fontWeight = FontWeight.Bold,
                        color = if (vencido) Color(0xFFB71C1C) else Color(0xFF1B5E20)
                    )
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (vencido) Color(0xFFE53935) else Color(0xFF43A047)
                    )
                    Text(
                        "Realizado a ${mantenimiento.kilometers} km · Cada ${mantenimiento.intervaloKms} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            // ──────────────────────────────────────────────────────

            DetalleCampo("Nombre", mantenimiento.maintenanceName)
            DetalleCampo("Fecha", mantenimiento.date)
            DetalleCampo("Precio", mantenimiento.price ?: "-")
            DetalleCampo("Tiempo", mantenimiento.time ?: "-")
            DetalleCampo("Notas", mantenimiento.notes ?: "-")
            DetalleCampo("Tareas", mantenimiento.tasks ?: "-")

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun DetalleCampo(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}