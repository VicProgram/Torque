package com.example.torque.components.ui.dashboard

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.torque.components.ui.dashboard.DashboardBottomNav
import com.example.torque.components.ui.dashboard.TorqueButton
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.components.ui.dashboard.showToast
import com.example.torque.garage.AgregarMoto
import com.example.torque.mantenimientos.Mantenimiento

@Composable
fun TorqueDashboardScreen(
    onIrGaraje: () -> Unit,
    onIrMantenimientos: () -> Unit,
    onIrPerfil: () -> Unit
) {
    val context = LocalContext.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DashboardHeader(onIrPerfil)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = "Bienvenido,",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Rider",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    // "Agendar Servicio" now navigates directly to creating maintenance
                    NextServiceCard(onClick = {
                        context.startActivity(Intent(context, Mantenimiento::class.java))
                    })
                }

                item {
                    Column {
                        Text(
                            text = "ACCIONES RÁPIDAS",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            QuickActionCard(
                                title = "Garaje",
                                subtitle = "Mis Motos",
                                icon = Icons.Default.Motorcycle,
                                containerColor = TorqueColors.SecondaryContainer,
                                contentColor = TorqueColors.OnSecondaryContainer,
                                modifier = Modifier.weight(1f),
                                onClick = onIrGaraje
                            )
                            QuickActionCard(
                                title = "Mantenimiento",
                                subtitle = "Logs & Más",
                                icon = Icons.Default.History,
                                containerColor = TorqueColors.PrimaryContainer,
                                contentColor = TorqueColors.OnPrimaryContainer,
                                modifier = Modifier.weight(1f),
                                onClick = onIrMantenimientos
                            )
                        }
                    }
                }

                item {
                    StatsCard()
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        FloatingActionButton(
            onClick = { 
                context.startActivity(Intent(context, AgregarMoto::class.java))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 110.dp),
            containerColor = TorqueColors.PrimaryContainer,
            contentColor = TorqueColors.OnPrimaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun DashboardHeader(onProfileClick: () -> Unit) {
    val context = LocalContext.current
    var showSettingsMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showToast(context, "Menú lateral no implementado") }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TorqueColors.OnSurface)
            }
            Text(
                text = "Dashboard",
                color = TorqueColors.OnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showToast(context, "No tienes notificaciones") }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TorqueColors.OnSurfaceVariant
                )
            }
            Box {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, TorqueColors.Outline, CircleShape)
                        .clickable { showSettingsMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        tint = TorqueColors.OnSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = showSettingsMenu,
                    onDismissRequest = { showSettingsMenu = false },
                    modifier = Modifier.background(TorqueColors.SurfaceDark)
                ) {
                    DropdownMenuItem(
                        text = { Text("Mi Perfil", color = Color.White) },
                        onClick = { 
                            showSettingsMenu = false
                            onProfileClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ajustes de App", color = Color.White) },
                        onClick = { 
                            showSettingsMenu = false
                            showToast(context, "Ajustes abiertos")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cerrar Sesión", color = Color.Red) },
                        onClick = { 
                            showSettingsMenu = false
                            showToast(context, "Sesión cerrada")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun NextServiceCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(TorqueColors.SurfaceContainerHigh)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(28.dp))
            .clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.height(160.dp).background(TorqueColors.SurfaceVariant)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, TorqueColors.SurfaceContainerHigh)
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(TorqueColors.Primary)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PRÓXIMO",
                        color = TorqueColors.OnPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Mantenimiento",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tu Moto Principal",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "12",
                            color = TorqueColors.Primary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "DÍAS REST.",
                            color = TorqueColors.Outline,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CAMBIO DE ACEITE",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "85% DUE",
                            color = TorqueColors.Primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = 0.85f,
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                        color = TorqueColors.Primary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                TorqueButton(
                    text = "Agendar Servicio",
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(TorqueColors.SurfaceContainer)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(28.dp))
        }
        Column {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
fun StatsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TorqueColors.SurfaceContainer)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ESTADÍSTICAS",
                color = TorqueColors.Tertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = TorqueColors.Outline)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "4,822",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Kilómetros",
                color = TorqueColors.OnSurfaceVariant,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
