package com.example.torque.components.ui.dashboard

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.R
import com.example.torque.ajustes.AjustesActivity
import com.example.torque.garage.AgregarMoto

// Función de utilidad para navegar de forma segura
private fun safeNavigate(context: Context, activityClass: Class<*>) {
    try {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    } catch (ignored: Exception) {
        Toast.makeText(context, "Error al abrir: ${activityClass.simpleName}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun TorqueDashboardScreen(
    onIrGaraje: () -> Unit = {},
    onIrMantenimientos: () -> Unit = {},
    onIrPerfil: () -> Unit = {}
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
                            text = stringResource(id = R.string.welcome_prefix), // "Bienvenido,"
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(id = R.string.welcome_name), // "Rider"
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    NextServiceCard(onClick = onIrMantenimientos)
                }

                item {
                    Column {
                        Text(
                            text = stringResource(id = R.string.quick_actions_title), // "ACCIONES RÁPIDAS"
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            QuickActionCard(
                                title = stringResource(id = R.string.garage_title),
                                subtitle = stringResource(id = R.string.garage_subtitle),
                                icon = Icons.Default.Motorcycle,
                                containerColor = TorqueColors.SecondaryContainer,
                                contentColor = TorqueColors.OnSecondaryContainer,
                                modifier = Modifier.weight(1f),
                                onClick = onIrGaraje
                            )
                            QuickActionCard(
                                title = stringResource(id = R.string.maintenance_title),
                                subtitle = stringResource(id = R.string.maintenance_subtitle),
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
                // Abrir activity de agregar moto
                safeNavigate(context, AgregarMoto::class.java)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 110.dp),
            containerColor = TorqueColors.PrimaryContainer,
            contentColor = TorqueColors.OnPrimaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun DashboardHeader(onProfileClick: () -> Unit) {
    val context = LocalContext.current
    var showSettingsMenu by rememberSaveable { mutableStateOf(false) }
    
    val menuToast = stringResource(id = R.string.menu_toast)
    val noNotifications = stringResource(id = R.string.no_notifications)
    val loggedOut = stringResource(id = R.string.logged_out)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showToast(context, menuToast) }) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.menu), tint = TorqueColors.OnSurface)
            }
            Text(
                text = stringResource(id = R.string.dashboard_title),
                color = TorqueColors.OnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showToast(context, noNotifications) }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = stringResource(id = R.string.notifications),
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
                        contentDescription = stringResource(id = R.string.settings),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        tint = TorqueColors.OnSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = showSettingsMenu,
                    onDismissRequest = { showSettingsMenu = false },
                    modifier = Modifier.background(TorqueColors.SurfaceDark)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.my_profile), color = Color.White) },
                        onClick = {
                            showSettingsMenu = false
                            onProfileClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.app_settings), color = Color.White) },
                        onClick = {
                            showSettingsMenu = false
                            safeNavigate(context, AjustesActivity::class.java)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.logout), color = Color.Red) },
                        onClick = {
                            showSettingsMenu = false
                            showToast(context, loggedOut)
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
            Box(modifier = Modifier
                .height(160.dp)
                .background(TorqueColors.SurfaceVariant)) {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                TorqueColors.SurfaceContainerHigh
                            )
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
                        text = stringResource(id = R.string.upcoming_label),
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
                            text = stringResource(id = R.string.maintenance_title),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.your_primary_bike),
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
                            text = stringResource(id = R.string.days_left),
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
                            text = stringResource(id = R.string.oil_change),
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
                        progress = { 0.85f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = TorqueColors.Primary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                TorqueButton(
                    text = stringResource(id = R.string.schedule_service),
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
            Icon(
                icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
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
                text = stringResource(id = R.string.stats_title),
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
                text = stringResource(id = R.string.kilometers),
                color = TorqueColors.OnSurfaceVariant,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
