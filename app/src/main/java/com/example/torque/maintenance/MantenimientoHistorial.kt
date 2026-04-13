package com.example.torque.maintenance

import android.content.Intent
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MantenimientoEntity
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MantenimientoHistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = AppRepository.obtenerInstancia(application)

    private val _historial = MutableStateFlow<List<MantenimientoEntity>>(emptyList())
    val historial: StateFlow<List<MantenimientoEntity>> = _historial

    fun cargarHistorial(idMoto: Long? = null) {
        viewModelScope.launch {
            if (idMoto != null) {
                repositorio.obtenerMantenimientosPorMoto(idMoto).collect {
                    _historial.value = it
                }
            } else {
                // TODO: Implementar obtener todos los mantenimientos en el repositorio si es necesario
                _historial.value = emptyList()
            }
        }
    }
}

class MantenimientoHistorial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMoto = intent.getLongExtra("motoId", -1L).takeIf { it != -1L }
        setContent {
            TorqueTheme {
                PantallaHistorialMantenimiento(idMoto)
            }
        }
    }
}

@Composable
fun PantallaHistorialMantenimiento(idMoto: Long? = null) {
    val contexto = LocalContext.current
    val viewModel: MantenimientoHistorialViewModel = viewModel()
    val historial by viewModel.historial.collectAsState()
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var itemSeleccionado by remember { mutableStateOf<MantenimientoEntity?>(null) }

    LaunchedEffect(idMoto) { viewModel.cargarHistorial(idMoto) }

    val historialFiltrado = remember(historial, filtroSeleccionado) {
        if (filtroSeleccionado == "Todos") historial
        else historial.filter {
            it.nombreMantenimiento.contains(filtroSeleccionado, ignoreCase = true) ||
                    (it.notas?.contains(filtroSeleccionado, ignoreCase = true) ?: false) ||
                    (it.tareas?.contains(filtroSeleccionado, ignoreCase = true) ?: false)
        }
    }

    if (itemSeleccionado != null) {
        Dialog(onDismissRequest = { itemSeleccionado = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TorqueColors.SurfaceDark)
            ) {
                // Adaptar DetalleMantenimientoScreen para usar MantenimientoEntity
                DetalleMantenimientoEntityScreen(
                    mantenimiento = itemSeleccionado!!,
                    onBack = { itemSeleccionado = null }
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(TorqueColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (contexto as? Activity)?.finish() }) {
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
                        val filtros = listOf("Todos", "Aceite", "Filtro", "Neumático", "Cadena", "Frenos")
                        items(filtros) { filtro ->
                            FiltroHistorialChip(
                                texto = filtro,
                                estaSeleccionado = filtroSeleccionado == filtro,
                                onClick = { filtroSeleccionado = filtro }
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

                if (historialFiltrado.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (historial.isEmpty()) "Sin mantenimientos registrados" else "Sin resultados para \"$filtroSeleccionado\"",
                                color = TorqueColors.OnSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(historialFiltrado) { item ->
                        ItemLogHistorial(item, onClick = { itemSeleccionado = item })
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
                val intent = Intent(contexto, RegistroMantenimiento::class.java).apply {
                    putExtra("idMoto", idMoto ?: -1L)
                }
                contexto.startActivity(intent)
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
fun FiltroHistorialChip(texto: String, estaSeleccionado: Boolean, onClick: () -> Unit) {
    val colorFondo = if (estaSeleccionado) TorqueColors.SecondaryContainer else Color.Transparent
    val colorContenido = if (estaSeleccionado) TorqueColors.OnSecondaryContainer else TorqueColors.OnSurfaceVariant
    val colorBorde = if (estaSeleccionado) Color.Transparent else TorqueColors.Outline.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorFondo)
            .border(if (estaSeleccionado) 0.dp else 1.dp, colorBorde, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (estaSeleccionado) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = colorContenido)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(texto, color = colorContenido, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ItemLogHistorial(item: MantenimientoEntity, onClick: () -> Unit) {
    val icono = when {
        item.nombreMantenimiento.contains("aceite", ignoreCase = true) || 
        (item.tareas?.contains("aceite", ignoreCase = true) ?: false) -> Icons.Default.OilBarrel
        item.nombreMantenimiento.contains("neumático", ignoreCase = true) || 
        item.nombreMantenimiento.contains("rueda", ignoreCase = true) ||
        (item.tareas?.contains("neumático", ignoreCase = true) ?: false) -> Icons.Default.TireRepair
        item.nombreMantenimiento.contains("cadena", ignoreCase = true) || 
        item.nombreMantenimiento.contains("kit", ignoreCase = true) ||
        (item.tareas?.contains("cadena", ignoreCase = true) ?: false) -> Icons.Default.SettingsSuggest
        item.nombreMantenimiento.contains("luz", ignoreCase = true) ||
        item.nombreMantenimiento.contains("eléctrico", ignoreCase = true) ||
        item.nombreMantenimiento.contains("batería", ignoreCase = true) ||
        (item.tareas?.contains("eléctrico", ignoreCase = true) ?: false) -> Icons.Default.Bolt
        item.nombreMantenimiento.contains("freno", ignoreCase = true) ||
        (item.tareas?.contains("freno", ignoreCase = true) ?: false) -> Icons.Default.BackHand
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
            Icon(icono, contentDescription = null, tint = TorqueColors.OnSurfaceVariant)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(item.nombreMantenimiento, color = TorqueColors.OnSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Text("${item.fecha} · ${item.precio ?: "Sin precio"}", color = TorqueColors.OnSurfaceVariant, fontSize = 13.sp)
        }

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(20.dp))
            Text("${item.kms} km", color = TorqueColors.Outline, fontSize = 11.sp)
        }
    }
}

@Composable
fun DetalleMantenimientoEntityScreen(
    mantenimiento: MantenimientoEntity,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Detalle del Mantenimiento",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = mantenimiento.nombreMantenimiento, color = TorqueColors.Primary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "${mantenimiento.fecha} · ${mantenimiento.kms} kms", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        
        if (!mantenimiento.notas.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "NOTAS", color = TorqueColors.Outline, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = mantenimiento.notas, color = Color.White, fontSize = 16.sp)
        }

        if (!mantenimiento.tareas.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "TAREAS REALIZADAS", color = TorqueColors.Outline, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = mantenimiento.tareas, color = Color.White, fontSize = 16.sp)
        }

        if (!mantenimiento.precio.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "COSTE", color = TorqueColors.Outline, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = mantenimiento.precio, color = Color(0xFF81C784), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}