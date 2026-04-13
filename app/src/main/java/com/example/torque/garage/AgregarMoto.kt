package com.example.torque.garage

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.components.ui.dashboard.*
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.MotoEntity
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AgregarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                VistaAgregarMoto()
            }
        }
    }
}

@Composable
fun VistaAgregarMoto() {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var cilindrada by remember { mutableStateOf("") }
    var caballos by remember { mutableStateOf("") }
    var estilo by remember { mutableStateOf("") }
    var kilometros by remember { mutableStateOf("") }
    var uriImagen by remember { mutableStateOf<Uri?>(null) }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uriImagen = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Cabecera ---
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
                        tint = TorqueColors.OnSurface
                    )
                }
                Text(
                    text = "Añadir Motocicleta",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = TorqueColors.OnSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- Selector de Foto ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(TorqueColors.SurfaceVariant.copy(alpha = 0.2f))
                            .then(
                                if (uriImagen == null) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = TorqueColors.Outline,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                } else Modifier
                            )
                            .clickable { selectorImagen.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uriImagen != null) {
                            Image(
                                painter = rememberAsyncImagePainter(uriImagen),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.DirectionsBike,
                                    contentDescription = null,
                                    tint = TorqueColors.OnSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "Sin foto seleccionada",
                                    color = TorqueColors.OnSurfaceVariant,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Botón Añadir Foto
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, TorqueColors.Outline, CircleShape)
                            .clickable { selectorImagen.launch("image/*") }
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = TorqueColors.Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Añadir Foto",
                                color = TorqueColors.Primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // --- Campos de Entrada ---
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(marca, { marca = it }, "Marca *")
                    TorqueTextField(modelo, { modelo = it }, "Modelo *")

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(anio, { anio = it }, "Año", modifier = Modifier.weight(1f))
                        TorqueTextField(matricula, { matricula = it }, "Matrícula", modifier = Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(kilometros, { kilometros = it }, "Kms Actuales", modifier = Modifier.weight(1f))
                        TorqueTextField(cilindrada, { cilindrada = it }, "Cilindrada", modifier = Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(caballos, { caballos = it }, "CV", modifier = Modifier.weight(1f))
                        TorqueTextField(color, { color = it }, "Color", modifier = Modifier.weight(1f))
                    }

                    TorqueTextField(
                        value = estilo,
                        onValueChange = { estilo = it },
                        label = "Notas u Observaciones",
                        singleLine = false,
                        modifier = Modifier.height(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // --- Botón Guardar (Footer) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(TorqueColors.Background)
                .padding(24.dp)
        ) {
            TorqueButton(
                text = "Guardar Motocicleta",
                icon = Icons.Default.Save,
                onClick = {
                    if (marca.isBlank() || modelo.isBlank()) {
                        Toast.makeText(contexto, "Por favor, rellena los campos obligatorios", Toast.LENGTH_SHORT).show()
                        return@TorqueButton
                    }

                    scope.launch {
                        val fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        
                        val nuevaMoto = MotoEntity(
                            marca = marca.trim(),
                            modelo = modelo.trim(),
                            anio = anio.toIntOrNull() ?: 0,
                            matricula = matricula.trim(),
                            color = color.trim(),
                            cilindrada = cilindrada.trim(),
                            caballos = caballos.trim(),
                            estilo = estilo.trim(),
                            kilometros = kilometros.toIntOrNull() ?: 0,
                            uriFoto = uriImagen?.toString(),
                            creadoEn = fechaActual,
                            actualizadoEn = fechaActual
                        )

                        val id = repositorio.insertarMoto(nuevaMoto)
                        
                        withContext(Dispatchers.Main) {
                            if (id > 0) {
                                Toast.makeText(contexto, "¡Moto guardada con éxito!", Toast.LENGTH_SHORT).show()
                                (contexto as? Activity)?.finish()
                            } else {
                                Toast.makeText(contexto, "Error al guardar la moto", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
        }
    }
}