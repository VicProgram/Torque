package com.example.torque.maintenance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.DashboardBottomNav
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MotoEntity
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.launch

class OpcionesMantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoIdIntent = intent.getLongExtra("idMoto", -1L)

        setContent {
            TorqueTheme {
                PantallaOpcionesMantenimiento(
                    motoIdInicial = motoIdIntent,
                    onNuevoMantenimiento = { id ->
                        val intent = Intent(this, RegistroMantenimiento::class.java).apply {
                            putExtra("idMoto", id)
                        }
                        startActivity(intent)
                    },
                    onVerHistorial = { id ->
                        val intent = Intent(this, MantenimientoHistorial::class.java).apply {
                            putExtra("idMoto", id)
                        }
                        startActivity(intent)
                    },
                    onAtras = { finish() }
                )
            }
        }
    }
}

@Composable
fun PantallaOpcionesMantenimiento(
    motoIdInicial: Long,
    onNuevoMantenimiento: (Long) -> Unit,
    onVerHistorial: (Long) -> Unit,
    onAtras: () -> Unit
) {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val coroutineScope = rememberCoroutineScope()
    
    var motoSeleccionada by remember { mutableStateOf<MotoEntity?>(null) }

    LaunchedEffect(motoIdInicial) {
        if (motoIdInicial != -1L) {
            motoSeleccionada = repositorio.obtenerMotoPorId(motoIdInicial)
        } else {
            // Si no hay ID, intentamos pillar la principal
            repositorio.obtenerMotoPrincipalFlow().collect { principal ->
                motoSeleccionada = principal
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
                IconButton(onClick = onAtras) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Mantenimiento",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info de la moto seleccionada
                motoSeleccionada?.let { moto ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TorqueColors.SurfaceDark),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Motorcycle, contentDescription = null, tint = TorqueColors.Primary)
                            Column {
                                Text("${moto.marca} ${moto.modelo}", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("${moto.kilometros} km", color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp)
                            }
                        }
                    }

                    TarjetaOpcionMantenimiento(
                        titulo = "Nuevo Registro",
                        subtitulo = "Registrar tareas realizadas",
                        icono = Icons.Default.Build,
                        color = TorqueColors.Primary,
                        onClick = { onNuevoMantenimiento(moto.idMoto) }
                    )

                    TarjetaOpcionMantenimiento(
                        titulo = "Ver Historial",
                        subtitulo = "Consulta registros pasados",
                        icono = Icons.Default.History,
                        color = TorqueColors.Accent,
                        onClick = { onVerHistorial(moto.idMoto) }
                    )
                } ?: run {
                    Text("No hay ninguna moto seleccionada", color = Color.White)
                }
            }
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun TarjetaOpcionMantenimiento(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(TorqueColors.SurfaceDark)
            .clickable(onClick = onClick)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
        }
        Column {
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(subtitulo, color = TorqueColors.OnSurfaceVariant, fontSize = 14.sp)
        }
    }
}