package com.example.torque.components.ui.dashboard

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.R
import com.example.torque.ajustes.AjustesActivity
import com.example.torque.garage.AnadirMotoActivity
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MotoEntity

@Composable
fun TorqueDashboardScreen(
    onIrGaraje: () -> Unit = {},
    onIrMantenimientos: (Long?) -> Unit = {},
    onIrPerfil: () -> Unit = {}
) {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val motoPrincipal by repositorio.obtenerMotoPrincipalFlow().collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp) // Espacio para la barra de navegación reducida
        ) {
            CabeceraDashboard(onIrPerfil)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp), // Reducido el espacio entre tarjetas
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = stringResource(id = R.string.welcome_prefix),
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 12.sp, // Ligeramente más pequeño
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(id = R.string.welcome_name),
                            color = Color.White,
                            fontSize = 28.sp, // Reducido de 32sp
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    TarjetaProximoServicio(moto = motoPrincipal, onClick = { onIrMantenimientos(motoPrincipal?.idMoto) })
                }

                item {
                    Column {
                        Text(
                            text = stringResource(id = R.string.quick_actions_title),
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 10.sp, // Reducido
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp) // Reducido
                        ) {
                            TarjetaAccionRapida(
                                titulo = stringResource(id = R.string.garage_title),
                                subtitulo = stringResource(id = R.string.garage_subtitle),
                                icono = Icons.Default.Motorcycle,
                                colorContenedor = TorqueColors.SecondaryContainer,
                                colorContenido = TorqueColors.OnSecondaryContainer,
                                modifier = Modifier.weight(1f),
                                onClick = onIrGaraje
                            )
                            TarjetaAccionRapida(
                                titulo = stringResource(id = R.string.maintenance_title),
                                subtitulo = stringResource(id = R.string.maintenance_subtitle),
                                icono = Icons.Default.History,
                                colorContenedor = TorqueColors.PrimaryContainer,
                                colorContenido = TorqueColors.OnPrimaryContainer,
                                modifier = Modifier.weight(1f),
                                onClick = { onIrMantenimientos(motoPrincipal?.idMoto) }
                            )
                        }
                    }
                }

                item {
                    TarjetaEstadisticas(moto = motoPrincipal)
                }
            }
        }

        FloatingActionButton(
            onClick = {
                contexto.startActivity(Intent(contexto, AnadirMotoActivity::class.java))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp), // Ajustada la posición del FAB
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
fun CabeceraDashboard(onIrPerfil: () -> Unit) {
    val contexto = LocalContext.current
    var mostrarMenuAjustes by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Menú Lateral */ }) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = TorqueColors.OnSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Torque",
                color = TorqueColors.OnSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Notificaciones */ }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = TorqueColors.OnSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Box {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, TorqueColors.Outline.copy(alpha = 0.5f), CircleShape)
                        .clickable { mostrarMenuAjustes = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        tint = TorqueColors.OnSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = mostrarMenuAjustes,
                    onDismissRequest = { mostrarMenuAjustes = false },
                    modifier = Modifier.background(TorqueColors.SurfaceDark)
                ) {
                    DropdownMenuItem(
                        text = { Text("Mi Perfil", color = Color.White) },
                        onClick = {
                            mostrarMenuAjustes = false
                            onIrPerfil()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ajustes", color = Color.White) },
                        onClick = {
                            mostrarMenuAjustes = false
                            contexto.startActivity(Intent(contexto, AjustesActivity::class.java))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun TarjetaProximoServicio(moto: MotoEntity?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)) // Reducido de 28.dp
            .background(TorqueColors.SurfaceContainerHigh)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(140.dp) // Reducido de 160.dp
                    .background(TorqueColors.SurfaceVariant)
            ) {
                if (moto?.uriFoto != null) {
                    Image(
                        painter = rememberAsyncImagePainter(moto.uriFoto),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier
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
                        .padding(12.dp) // Reducido de 16.dp
                        .clip(CircleShape)
                        .background(TorqueColors.Primary)
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PRÓXIMO SERVICIO",
                        color = TorqueColors.OnPrimary,
                        fontSize = 10.sp, // Reducido de 11.sp
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(20.dp)) { // Reducido de 24.dp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = moto?.modelo ?: "Sin Moto",
                            color = Color.White,
                            fontSize = 20.sp, // Reducido de 24.sp
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (moto != null) "Tu moto principal" else "Añade una moto",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 12.sp, // Reducido de 14.sp
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "12",
                            color = TorqueColors.Primary,
                            fontSize = 28.sp, // Reducido de 32.sp
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "DÍAS",
                            color = TorqueColors.Outline,
                            fontSize = 9.sp, // Reducido de 10.sp
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp)) // Reducido de 20.dp
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ESTADO GENERAL",
                            color = TorqueColors.OnSurfaceVariant,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "85%",
                            color = TorqueColors.Primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Reducido de 12.dp
                    LinearProgressIndicator(
                        progress = { 0.85f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp) // Reducido de 10.dp
                            .clip(CircleShape),
                        color = TorqueColors.Primary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TorqueButton(
                    text = "Ver Mantenimiento",
                    onClick = onClick,
                    modifier = Modifier.height(48.dp) // Reducido de 56.dp (vía default en TorqueButton, aquí forzamos)
                )
            }
        }
    }
}

@Composable
fun TarjetaAccionRapida(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorContenedor: Color,
    colorContenido: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)) // Reducido de 16.dp
            .background(TorqueColors.SurfaceContainer)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp), // Reducido de 20.dp
        verticalArrangement = Arrangement.spacedBy(8.dp) // Reducido de 16.dp
    ) {
        Box(
            modifier = Modifier
                .size(40.dp) // Reducido de 48.dp
                .clip(RoundedCornerShape(12.dp))
                .background(colorContenedor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = colorContenido,
                modifier = Modifier.size(24.dp) // Reducido de 28.dp
            )
        }
        Column {
            Text(text = titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold) // Reducido de 16.sp
            Text(text = subtitulo, color = TorqueColors.OnSurfaceVariant, fontSize = 11.sp) // Reducido de 12.sp
        }
    }
}

@Composable
fun TarjetaEstadisticas(moto: MotoEntity?) {
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
                text = "KILOMETRAJE TOTAL",
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
                text = moto?.kilometros?.toString() ?: "0",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "KILÓMETROS",
                color = TorqueColors.OnSurfaceVariant,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}