package com.example.torque.garage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.torque.components.ui.dashboard.launchActivity
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

class ListaGarage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                ListaGarageView()
            }
        }
    }
}

@Composable
fun ListaGarageView() {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }
    val motos = remember { mutableStateListOf<Moto>() }

    fun reloadMotos() {
        motos.clear()
        motos.addAll(dbHelper.getMotos())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        reloadMotos()
    }

    LaunchedEffect(Unit) {
        reloadMotos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GarageHeader()

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
                        item { FilterChip("All", isSelected = true) }
                        item { FilterChip("Maintenance", isSelected = false) }
                        item { FilterChip("Archive", isSelected = false) }
                    }
                }

                items(motos) { moto ->
                    MotoCard(
                        moto = moto,
                        onClick = {
                            launchActivity(launcher, context, MotoDetalle::class.java) {
                                putExtra("idMoto", moto.idMoto)
                            }
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                launchActivity(launcher, context, AgregarMoto::class.java)
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
                contentDescription = "New Moto",
                modifier = Modifier.size(24.dp)
            )
        }

        DashboardBottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun GarageHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(TorqueColors.Background.copy(alpha = 0.95f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Garage",
            color = TorqueColors.OnSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal
        )
        Row {
            IconButton(onClick = { /* Search */ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TorqueColors.OnSurfaceVariant
                )
            }
            IconButton(onClick = { /* More */ }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = TorqueColors.OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean) {
    val bgColor = if (isSelected) TorqueColors.PrimaryContainer else Color.Transparent
    val contentColor =
        if (isSelected) TorqueColors.OnPrimaryContainer else TorqueColors.OnSurfaceVariant
    val borderColor = if (isSelected) Color.Transparent else TorqueColors.Outline

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { /* Select */ }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MotoCard(moto: Moto, onClick: () -> Unit) {
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
            if (moto.fotoMoto.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(moto.fotoMoto),
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
            Text(
                text = moto.modelo,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )
            Text(
                text = "${moto.marca} • ${moto.anno}",
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
                        imageVector = if (moto.idMoto % 2 == 0) Icons.Default.CalendarToday else Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (moto.idMoto % 2 == 0) TorqueColors.Primary else Color(0xFF34D399)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (moto.idMoto % 2 == 0) "Service in 15d" else "Healthy Status",
                        color = if (moto.idMoto % 2 == 0) TorqueColors.Primary else Color(0xFF34D399),
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
                        text = "View Details",
                        color = TorqueColors.Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}