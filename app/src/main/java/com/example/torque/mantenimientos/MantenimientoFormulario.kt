package com.example.torque.mantenimientos

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Date
import java.util.Locale

class MantenimientoFormulario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val seleccionados = intent.getStringArrayListExtra("seleccionados") ?: arrayListOf()
        setContent {
            TorqueTheme {
                MantenimientoFormularioScreen(seleccionados)
            }
        }
    }
}

@Composable
fun MantenimientoFormularioScreen(seleccionados: List<String>) {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }
    val scrollState = rememberScrollState()

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anno by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

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
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Atrás", tint = TorqueColors.Primary)
                }
                Text(
                    text = "Detalles Finales",
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
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    "Tareas: ${seleccionados.joinToString(", ")}",
                    color = TorqueColors.OnSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TorqueTextField(marca, { marca = it }, "Marca")
                TorqueTextField(modelo, { modelo = it }, "Modelo")
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(anno, { anno = it }, "Año", modifier = Modifier.weight(1f))
                    TorqueTextField(km, { km = it }, "Kilómetros", modifier = Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(precio, { precio = it }, "Precio ($)", modifier = Modifier.weight(1f))
                    TorqueTextField(tiempo, { tiempo = it }, "Tiempo", modifier = Modifier.weight(1f))
                }

                TorqueTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = "Notas Adicionales",
                    singleLine = false,
                    modifier = Modifier.height(120.dp)
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // --- Save Button Footer ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(TorqueColors.Background)
                .padding(24.dp)
        ) {
            TorqueButton(
                text = "Finalizar Registro",
                icon = Icons.Default.Save,
                onClick = {
                    val mantenimiento = MantenimientoDetalle(
                        nombreMantenimiento = if (seleccionados.size == 1) seleccionados[0] else "Mantenimiento General",
                        date = fechaActual,
                        kilometers = km.toIntOrNull(),
                        marca = marca,
                        modelo = modelo,
                        anno = anno.toIntOrNull(),
                        precio = precio,
                        tiempo = tiempo,
                        notas = notas,
                        tareas = seleccionados.joinToString(", ")
                    )

                    dbHelper.insertarMantenimiento(mantenimiento)
                    showToast(context, "¡Mantenimiento guardado!")
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}
