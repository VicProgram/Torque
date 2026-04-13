package com.example.torque.garage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.components.ui.dashboard.DashboardBottomNav
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MotoEntity
import com.example.torque.ui.theme.TorqueTheme
import android.content.Intent

class ListaGaraje : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                VistaListaGaraje()
            }
        }
    }
}

@Composable
fun VistaListaGaraje() {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val motos by repositorio.obtenerMotosFlow().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CabeceraGaraje()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item { FiltroChip("Todas", estaSeleccionado = true) }
                        item { FiltroChip("Mantenimiento", estaSeleccionado = false) }
                        item { FiltroChip("Archivo", estaSeleccionado = false) }
                    }
                }

                items(motos) { moto ->
                    TarjetaMoto(
                        moto = moto,
                        onClick = {
                            val intent = Intent(contexto, MotoDetalle::class.java).apply {
                                putExtra("idMoto", moto.idMoto)
                            }
                            contexto.startActivity(intent)
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                contexto.startActivity(Intent(contexto, AgregarMoto::class.java))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 110.dp),
            containerColor = TorqueColors.Primary,
            contentColor = TorqueColors.OnPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Nueva Moto",
                modifier = Modifier.size(24.dp)
            )
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun CabeceraGaraje() {
    val contexto = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(TorqueColors.Background.copy(alpha = 0.95f))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { (contexto as? android.app.Activity)?.finish() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = TorqueColors.OnSurface
            )
        }
        Text(
            text = "Mi Garaje",
            color = TorqueColors.OnSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Row {
            IconButton(onClick = { /* Buscar */ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = TorqueColors.OnSurfaceVariant
                )
            }
            IconButton(onClick = { /* Más */ }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Más",
                    tint = TorqueColors.OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FiltroChip(texto: String, estaSeleccionado: Boolean) {
    val colorFondo = if (estaSeleccionado) TorqueColors.PrimaryContainer else Color.Transparent
    val colorContenido =
        if (estaSeleccionado) TorqueColors.OnPrimaryContainer else TorqueColors.OnSurfaceVariant
    val colorBorde = if (estaSeleccionado) Color.Transparent else TorqueColors.Outline

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorFondo)
            .border(
                width = if (estaSeleccionado) 0.dp else 1.dp,
                color = colorBorde,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { /* Seleccionar */ }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = texto,
            color = colorContenido,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TarjetaMoto(moto: MotoEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TorqueColors.SurfaceContainer)
            .border(1.dp, TorqueColors.Outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(TorqueColors.SurfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            val foto = moto.uriFoto ?: ""
            if (foto.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(foto),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.9f)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Motorcycle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = TorqueColors.OnSurfaceVariant.copy(alpha = 0.3f)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = moto.modelo,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    modifier = Modifier.weight(1f)
                )
                if (moto.esPrincipal == 1) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Principal",
                        tint = TorqueColors.Primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text(
                text = "${moto.marca} • ${moto.anio}",
                color = TorqueColors.OnSurfaceVariant,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF34D399)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estado Saludable",
                        color = Color(0xFF34D399),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, TorqueColors.Outline, CircleShape)
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Ver Detalles",
                        color = TorqueColors.Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}