package com.example.torque.maintenance

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MantenimientoEntity
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FormularioMantenimiento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val tareasSeleccionadas = intent.getStringArrayListExtra("seleccionados") ?: arrayListOf()
        val idMoto = intent.getLongExtra("idMoto", -1L)
        setContent {
            TorqueTheme {
                VistaFormularioMantenimiento(tareasSeleccionadas, idMoto)
            }
        }
    }
}

@Composable
fun VistaFormularioMantenimiento(tareasSeleccionadas: List<String>, idMoto: Long) {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val scope = rememberCoroutineScope()

    var kms by remember { mutableStateOf("") }
    var intervalo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (contexto as? Activity)?.finish() }) {
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
                            tareasSeleccionadas.joinToString(" · "),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(kms, { kms = it }, "Kms actuales", modifier = Modifier.weight(1f))
                    TorqueTextField(intervalo, { intervalo = it }, "Cada X kms", modifier = Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(precio, { precio = it }, "Precio (€)", modifier = Modifier.weight(1f))
                    TorqueTextField(tiempo, { tiempo = it }, "Tiempo", modifier = Modifier.weight(1f))
                }

                TorqueTextField(
                    value = notas,
                    onValueChange = { notas = it },
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
                    if (idMoto == -1L) {
                        Toast.makeText(contexto, "Error: No se identificó la moto", Toast.LENGTH_SHORT).show()
                        return@TorqueButton
                    }

                    scope.launch {
                        val mantenimiento = MantenimientoEntity(
                            idMoto = idMoto,
                            nombreMantenimiento = if (tareasSeleccionadas.size == 1) tareasSeleccionadas[0] else "Mantenimiento General",
                            fecha = fechaActual,
                            kms = kms.toIntOrNull() ?: 0,
                            intervaloKms = intervalo.toIntOrNull() ?: 0,
                            precio = precio,
                            tiempo = tiempo,
                            notas = notas,
                            tareas = tareasSeleccionadas.joinToString(", "),
                            creadoEn = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        )
                        repositorio.insertarMantenimiento(mantenimiento)
                        
                        launch(kotlinx.coroutines.Dispatchers.Main) {
                            Toast.makeText(contexto, "¡Mantenimiento guardado!", Toast.LENGTH_SHORT).show()
                            (contexto as? Activity)?.finish()
                        }
                    }
                }
            )
        }
    }
}