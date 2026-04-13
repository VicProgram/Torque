package com.example.torque.maintenance

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.TorqueButton
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.components.ui.dashboard.TorqueTextField
import com.example.torque.components.ui.dashboard.showToast
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import java.text.SimpleDateFormat
import java.util.*

class MaintenanceForm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedTasks = intent.getStringArrayListExtra("seleccionados") ?: arrayListOf()
        val motoId = intent.getLongExtra("motoId", 0L)
        setContent {
            TorqueTheme {
                MaintenanceFormScreen(selectedTasks, motoId)
            }
        }
    }
}

@Composable
fun MaintenanceFormScreen(selectedTasks: List<String>, motoId: Long) {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }

    var km by remember { mutableStateOf("") }
    var intervalo by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TorqueColors.Primary
                    )
                }
                Text(
                    text = "Detalles del Mantenimiento",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Tareas seleccionadas
                Card(
                    colors = CardDefaults.cardColors(containerColor = TorqueColors.SurfaceDark),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tareas", color = TorqueColors.Slate500, fontSize = 12.sp)
                        Text(
                            selectedTasks.joinToString(" · "),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(km, { km = it }, "Kms actuales", modifier = Modifier.weight(1f))
                    TorqueTextField(intervalo, { intervalo = it }, "Cada X kms", modifier = Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(price, { price = it }, "Precio (€)", modifier = Modifier.weight(1f))
                    TorqueTextField(time, { time = it }, "Tiempo", modifier = Modifier.weight(1f))
                }

                TorqueTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Notas",
                    singleLine = false,
                    modifier = Modifier.height(120.dp)
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(TorqueColors.Background)
                .padding(24.dp)
        ) {
            TorqueButton(
                text = "Guardar Mantenimiento",
                icon = Icons.Default.Save,
                onClick = {
                    val maintenance = MaintenanceDetailData(
                        motoId = motoId,
                        maintenanceName = if (selectedTasks.size == 1) selectedTasks[0] else "Mantenimiento General",
                        date = currentDate,
                        kilometers = km.toIntOrNull() ?: 0,
                        intervaloKms = intervalo.toIntOrNull() ?: 0,
                        price = price,
                        time = time,
                        notes = notes,
                        tasks = selectedTasks.joinToString(", ")
                    )
                    dbHelper.insertMaintenance(maintenance)
                    showToast(context, "¡Mantenimiento guardado!")
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}