package com.example.torque.maintenance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DetalleMantenimientoScreen(
    mantenimiento: MaintenanceDetailData,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "Detalles del Mantenimiento",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetalleCampo("Nombre", mantenimiento.maintenanceName)
            DetalleCampo("Fecha", mantenimiento.date)
            DetalleCampo("Kilómetros", mantenimiento.kilometers?.toString() ?: "")
            DetalleCampo("Marca", mantenimiento.brand ?: "")
            DetalleCampo("Modelo", mantenimiento.model ?: "")
            DetalleCampo("Año", mantenimiento.year?.toString() ?: "")
            DetalleCampo("Precio", mantenimiento.price ?: "")
            DetalleCampo("Tiempo", mantenimiento.time ?: "")
            DetalleCampo("Notas", mantenimiento.notes ?: "")
            DetalleCampo("Tareas", mantenimiento.tasks ?: "")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun DetalleCampo(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
