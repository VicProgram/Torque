package com.example.torque.maintenance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.MotoKeepCheckItem
import com.example.torque.components.ui.dashboard.SectionHeader
import com.example.torque.components.ui.dashboard.TorqueButton
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.ui.theme.TorqueTheme

class MaintenanceLog : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                //  MaintenanceLogScreen()
            }
        }
    }
}

data class Task(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val category: String
)

@Composable
fun MaintenanceLogScreen() {
    val context = LocalContext.current
    val checkedItems = remember { mutableStateMapOf<String, Boolean>() }
    var searchQuery by remember { mutableStateOf("") }

    val commonTasks = listOf(
        Task(
            "Cambio de Aceite Motor",
            "Recomendado cada 5,000 km",
            Icons.Default.OilBarrel,
            Color(0xFF3B82F6),
            "Engine"
        ),
        Task(
            "Filtro de Aceite",
            "Incluido en servicio estándar",
            Icons.Default.FilterAlt,
            Color(0xFFF97316),
            "Engine"
        ),
        Task(
            "Limpieza y Engrase de Cadena",
            "Mantenimiento regular",
            Icons.Default.SettingsInputComponent,
            Color(0xFF6366F1),
            "Drivetrain"
        )
    )

    val safetyTasks = listOf(
        Task(
            "Presión de Neumáticos",
            "Esencial para seguridad",
            Icons.Default.TireRepair,
            Color(0xFF10B981),
            "Tires"
        ),
        Task(
            "Inspección Pastillas Freno",
            "Revisar indicadores de desgaste",
            Icons.Default.CarRepair,
            Color(0xFFEF4444),
            "Brakes"
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
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { (context as? ComponentActivity)?.finish() },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = null,
                            tint = TorqueColors.Primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text("Atrás", color = TorqueColors.Primary, fontSize = 18.sp)
                    }
                }
                Text(
                    "Registrar Mantenimiento",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(60.dp))
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
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
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
                            item { FilterChip("Todas", true) }
                            item { FilterChip("Motor", false) }
                            item { FilterChip("Transmisión", false) }
                            item { FilterChip("Frenos", false) }
                            item { FilterChip("Ruedas", false) }
                        }
                    }
                }

                item { SectionHeader("Mantenimiento Común") }
                items(commonTasks) { task ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        MotoKeepCheckItem(
                            title = task.name,
                            description = task.description,
                            icon = task.icon,
                            iconColor = task.iconColor,
                            isChecked = checkedItems[task.name] == true,
                            onCheckedChange = { checkedItems[task.name] = it }
                        )
                    }
                }

                item { SectionHeader("Inspección de Seguridad") }
                items(safetyTasks) { task ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        MotoKeepCheckItem(
                            title = task.name,
                            description = task.description,
                            icon = task.icon,
                            iconColor = task.iconColor,
                            isChecked = checkedItems[task.name] == true,
                            onCheckedChange = { checkedItems[task.name] = it }
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
                val selectedCount = checkedItems.values.count { it }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$selectedCount tareas seleccionadas",
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
                    text = "Registrar Mantenimiento",
                    color = TorqueColors.Accent,
                    icon = Icons.Default.CheckCircle,
                    onClick = {
                        val selectedList = checkedItems.filterValues { it }.keys.toList()
                        if (selectedList.isNotEmpty()) {
                            val intent = Intent(context, MaintenanceForm::class.java).apply {
                                putStringArrayListExtra("seleccionados", ArrayList(selectedList))
                            }
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean) {
    val bgColor = if (isSelected) TorqueColors.Primary else TorqueColors.SurfaceDark
    val contentColor = if (isSelected) Color.White else TorqueColors.OnSurfaceVariant
    val borderColor = if (isSelected) Color.Transparent else TorqueColors.BorderDark

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}
