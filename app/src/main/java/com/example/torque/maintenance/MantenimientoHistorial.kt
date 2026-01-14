package com.example.torque.maintenance

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.torque.components.ui.dashboard.DashboardBottomNav
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ---------- ViewModel ----------
class MantenimientoHistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = TorqueDatabaseHelper(application)

    private val _historial = MutableStateFlow<List<MaintenanceDetail>>(emptyList())
    val historial: StateFlow<List<MaintenanceDetail>> = _historial

    fun cargarHistorial() {
        viewModelScope.launch {
            _historial.value = TorqueDatabaseHelper(getApplication()).getMaintenances()
        }
    }
}

class MantenimientoHistorial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MantenimientoHistorialPantalla()
            }
        }
    }
}

@Composable
fun MantenimientoHistorialPantalla() {
    val context = LocalContext.current
    val viewModel: MantenimientoHistorialViewModel = viewModel()
    val historial by viewModel.historial.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial()
    }

    val filteredHistorial = remember(historial, selectedFilter) {
        if (selectedFilter == "Todos") {
            historial
        } else {
            historial.filter { item ->
                when (selectedFilter) {
                    "Aceite" -> item.maintenanceName.contains("aceite", ignoreCase = true)
                    "Neumáticos" -> item.maintenanceName.contains(
                        "neumático",
                        ignoreCase = true
                    )

                    "Motor" -> item.maintenanceName.contains("motor", ignoreCase = true)
                    "Cadena" -> item.maintenanceName.contains("cadena", ignoreCase = true)
                    else -> true
                }
            }
        }
    }

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
                        Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TorqueColors.OnSurface
                    )
                }
                Text(
                    text = "Historial",
                    modifier = Modifier.weight(1f),
                    color = TorqueColors.OnSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
                IconButton(onClick = { /* More options */ }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Más",
                        tint = TorqueColors.OnSurface
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                // --- Filter Chips ---
                item {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("Todos", "Aceite", "Neumáticos", "Motor", "Cadena")
                        items(filters) { filter ->
                            HistoryFilterChip(
                                text = filter,
                                isSelected = selectedFilter == filter,
                                onClick = { selectedFilter = filter }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "ACTIVIDAD RECIENTE",
                        color = TorqueColors.Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                if (filteredHistorial.isEmpty()) {
                    item {
                        Text(
                            text = if (historial.isEmpty()) "No hay mantenimientos registrados." else "No hay resultados para este filtro.",
                            color = TorqueColors.OnSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(filteredHistorial) { item ->
                        HistoryLogItem(item)
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = TorqueColors.SurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }

        // --- FAB ---
        FloatingActionButton(
            onClick = { /* Add activity */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 110.dp),
            containerColor = TorqueColors.PrimaryContainer,
            contentColor = TorqueColors.OnPrimaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir", modifier = Modifier.size(24.dp))
        }

        // --- Bottom Navigation ---
        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HistoryFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) TorqueColors.SecondaryContainer else Color.Transparent
    val contentColor =
        if (isSelected) TorqueColors.OnSecondaryContainer else TorqueColors.OnSurfaceVariant
    val borderColor = if (isSelected) Color.Transparent else TorqueColors.Outline.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun HistoryLogItem(item: MaintenanceDetail) {
    // Determine icon based on name (simple logic)
    val icon = when {
        item.maintenanceName.contains("aceite", ignoreCase = true) -> Icons.Default.OilBarrel
        item.maintenanceName.contains(
            "neumático",
            ignoreCase = true
        ) -> Icons.Default.TireRepair

        item.maintenanceName.contains(
            "cadena",
            ignoreCase = true
        ) -> Icons.Default.SettingsSuggest

        item.maintenanceName.contains(
            "luz",
            ignoreCase = true
        ) || item.maintenanceName.contains("eléctrico", ignoreCase = true) -> Icons.Default.Bolt

        else -> Icons.Default.Build
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Show details */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(TorqueColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = TorqueColors.OnSurfaceVariant)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.maintenanceName,
                color = TorqueColors.OnSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Text(
                text = "${item.date} • ${item.price ?: "$0.00"}",
                color = TorqueColors.OnSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Completado",
                tint = Color(0xFF81C784),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${item.kilometers ?: 0} km",
                color = TorqueColors.Outline,
                fontSize = 11.sp
            )
        }
    }
}
