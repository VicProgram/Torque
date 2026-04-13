package com.example.torque.maintenance

import android.content.Intent
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

class MantenimientoHistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = TorqueDatabaseHelper(application)

    private val _historial = MutableStateFlow<List<MaintenanceDetailData>>(emptyList())
    val historial: StateFlow<List<MaintenanceDetailData>> = _historial

    fun cargarHistorial(motoId: Long? = null) {
        viewModelScope.launch {
            _historial.value = if (motoId != null)
                dbHelper.getMantenimientosByMoto(motoId)
            else
                dbHelper.getAllMantenimientos()
        }
    }
}

class MantenimientoHistorial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoId = intent.getLongExtra("motoId", -1L).takeIf { it != -1L }
        setContent {
            TorqueTheme {
                MantenimientoHistorialPantalla(motoId)
            }
        }
    }
}

@Composable
fun MantenimientoHistorialPantalla(motoId: Long? = null) {
    val context = LocalContext.current
    val viewModel: MantenimientoHistorialViewModel = viewModel()
    val historial by viewModel.historial.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedItem by remember { mutableStateOf<MaintenanceDetailData?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.cargarHistorial(motoId)
    }

    LaunchedEffect(Unit) { viewModel.cargarHistorial(motoId) }

    val filteredHistorial = remember(historial, selectedFilter) {
        if (selectedFilter == "Todos") historial
        else historial.filter {
            it.maintenanceName.contains(selectedFilter, ignoreCase = true) ||
                    (it.notes?.contains(selectedFilter, ignoreCase = true) ?: false) ||
                    (it.tasks?.contains(selectedFilter, ignoreCase = true) ?: false)
        }
    }

    if (selectedItem != null) {
        Dialog(onDismissRequest = { selectedItem = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TorqueColors.SurfaceDark)
            ) {
                DetalleMantenimientoScreen(
                    mantenimiento = selectedItem!!,
                    kmsActualesMoto = 0, // TODO: Pasarlo si es necesario
                    onBack = { selectedItem = null }
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(TorqueColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TorqueColors.OnSurface
                    )
                }
                Text(
                    text = "Historial de Mantenimientos",
                    modifier = Modifier.weight(1f),
                    color = TorqueColors.OnSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = 140.dp)) {
                // Filtros
                item {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("Todos", "Aceite", "Filtro", "Neumático", "Cadena", "Frenos")
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
                        "ACTIVIDAD RECIENTE",
                        color = TorqueColors.Primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                if (filteredHistorial.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (historial.isEmpty()) "Sin mantenimientos registrados" else "Sin resultados para \"$selectedFilter\"",
                                color = TorqueColors.OnSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredHistorial) { item ->
                        HistoryLogItem(item, onClick = { selectedItem = item })
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = TorqueColors.SurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                val intent = Intent(context, MaintenanceLog::class.java).apply {
                    putExtra("motoId", motoId ?: -1L)
                }
                launcher.launch(intent)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 110.dp),
            containerColor = TorqueColors.PrimaryContainer,
            contentColor = TorqueColors.OnPrimaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir")
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HistoryFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) TorqueColors.SecondaryContainer else Color.Transparent
    val contentColor = if (isSelected) TorqueColors.OnSecondaryContainer else TorqueColors.OnSurfaceVariant
    val borderColor = if (isSelected) Color.Transparent else TorqueColors.Outline.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(if (isSelected) 0.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = contentColor)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(text, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun HistoryLogItem(item: MaintenanceDetailData, onClick: () -> Unit) {
        val icon = when {
            item.maintenanceName.contains("aceite", ignoreCase = true) || 
            (item.tasks?.contains("aceite", ignoreCase = true) ?: false) -> Icons.Default.OilBarrel
            item.maintenanceName.contains("neumático", ignoreCase = true) || 
            item.maintenanceName.contains("rueda", ignoreCase = true) ||
            (item.tasks?.contains("neumático", ignoreCase = true) ?: false) -> Icons.Default.TireRepair
            item.maintenanceName.contains("cadena", ignoreCase = true) || 
            item.maintenanceName.contains("kit", ignoreCase = true) ||
            (item.tasks?.contains("cadena", ignoreCase = true) ?: false) -> Icons.Default.SettingsSuggest
            item.maintenanceName.contains("luz", ignoreCase = true) ||
            item.maintenanceName.contains("eléctrico", ignoreCase = true) ||
            item.maintenanceName.contains("batería", ignoreCase = true) ||
            (item.tasks?.contains("eléctrico", ignoreCase = true) ?: false) -> Icons.Default.Bolt
            item.maintenanceName.contains("freno", ignoreCase = true) ||
            (item.tasks?.contains("freno", ignoreCase = true) ?: false) -> Icons.Default.BackHand
            else -> Icons.Default.Build
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(TorqueColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = TorqueColors.OnSurfaceVariant)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(item.maintenanceName, color = TorqueColors.OnSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Text("${item.date} · ${item.price ?: "Sin precio"}", color = TorqueColors.OnSurfaceVariant, fontSize = 13.sp)
        }

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(20.dp))
            Text("${item.kilometers} km", color = TorqueColors.Outline, fontSize = 11.sp)
        }
    }
}