package com.example.torque.maintenance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.*
import com.example.torque.ui.theme.TorqueTheme

class RegistroMantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMoto = intent.getLongExtra("idMoto", -1L)
        setContent {
            TorqueTheme {
                PantallaRegistroMantenimiento(idMoto)
            }
        }
    }
}

data class Tarea(
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val categoria: String
)

@Composable
fun PantallaRegistroMantenimiento(idMoto: Long) {
    val contexto = LocalContext.current
    val itemsSeleccionados = remember { mutableStateMapOf<String, Boolean>() }
    var consultaBusqueda by remember { mutableStateOf("") }

    val tareasComunes = listOf(
        Tarea(
            "Cambio de Aceite Motor",
            "Recomendado cada 5,000 km",
            Icons.Default.OilBarrel,
            Color(0xFF3B82F6),
            "Motor"
        ),
        Tarea(
            "Filtro de Aceite",
            "Incluido en servicio estándar",
            Icons.Default.FilterAlt,
            Color(0xFFF97316),
            "Motor"
        ),
        Tarea(
            "Limpieza y Engrase de Cadena",
            "Mantenimiento regular",
            Icons.Default.SettingsInputComponent,
            Color(0xFF6366F1),
            "Transmisión"
        )
    )

    val tareasSeguridad = listOf(
        Tarea(
            "Presión de Neumáticos",
            "Esencial para seguridad",
            Icons.Default.TireRepair,
            Color(0xFF10B981),
            "Neumáticos"
        ),
        Tarea(
            "Inspección Pastillas Freno",
            "Revisar indicadores de desgaste",
            Icons.Default.CarRepair,
            Color(0xFFEF4444),
            "Frenos"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (contexto as? android.app.Activity)?.finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TorqueColors.Primary
                    )
                }
                Text(
                    "Registrar Mantenimiento",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 160.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = consultaBusqueda,
                            onValueChange = { consultaBusqueda = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    "Buscar tareas...",
                                    color = TorqueColors.Slate500
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = TorqueColors.Slate500
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = TorqueColors.SurfaceDark,
                                unfocusedContainerColor = TorqueColors.SurfaceDark,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = TorqueColors.Primary,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item { ChipFiltro("Todas", true) }
                            item { ChipFiltro("Motor", false) }
                            item { ChipFiltro("Transmisión", false) }
                            item { ChipFiltro("Frenos", false) }
                            item { ChipFiltro("Ruedas", false) }
                        }
                    }
                }

                item { SectionHeader("Mantenimiento Común") }
                items(tareasComunes) { tarea ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        MotoKeepCheckItem(
                            title = tarea.nombre,
                            description = tarea.descripcion,
                            icon = tarea.icono,
                            iconColor = tarea.colorIcono,
                            isChecked = itemsSeleccionados[tarea.nombre] == true,
                            onCheckedChange = { itemsSeleccionados[tarea.nombre] = it }
                        )
                    }
                }

                item { SectionHeader("Inspección de Seguridad") }
                items(tareasSeguridad) { tarea ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        MotoKeepCheckItem(
                            title = tarea.nombre,
                            description = tarea.descripcion,
                            icon = tarea.icono,
                            iconColor = tarea.colorIcono,
                            isChecked = itemsSeleccionados[tarea.nombre] == true,
                            onCheckedChange = { itemsSeleccionados[tarea.nombre] = it }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            TorqueColors.Background.copy(alpha = 0.95f),
                            TorqueColors.Background
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(TorqueColors.SurfaceDark)
                    .border(1.dp, TorqueColors.BorderDark, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val cantidadSeleccionada = itemsSeleccionados.values.count { it }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$cantidadSeleccionada tareas seleccionadas",
                        color = TorqueColors.Slate500,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Estimado 45 min",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                TorqueButton(
                    text = "Continuar",
                    color = TorqueColors.Accent,
                    icon = Icons.Default.ArrowForward,
                    onClick = {
                        val listaSeleccionados = itemsSeleccionados.filterValues { it }.keys.toList()
                        if (listaSeleccionados.isNotEmpty()) {
                            val intent = Intent(contexto, FormularioMantenimiento::class.java).apply {
                                putStringArrayListExtra("seleccionados", ArrayList(listaSeleccionados))
                                putExtra("idMoto", idMoto)
                            }
                            contexto.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChipFiltro(texto: String, estaSeleccionado: Boolean) {
    val colorFondo = if (estaSeleccionado) TorqueColors.Primary else TorqueColors.SurfaceDark
    val colorContenido = if (estaSeleccionado) Color.White else TorqueColors.OnSurfaceVariant
    val colorBorde = if (estaSeleccionado) Color.Transparent else TorqueColors.BorderDark

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorFondo)
            .border(1.dp, colorBorde, CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = texto,
            color = colorContenido,
            fontSize = 14.sp,
            fontWeight = if (estaSeleccionado) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}