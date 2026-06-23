package com.example.torque.garage

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
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
import com.example.torque.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AnadirMotoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TorqueTheme {
                VistaAnadirMoto()
            }
        }
    }
}

@Composable
fun VistaAnadirMoto() {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var cilindrada by remember { mutableStateOf("") }
    var caballos by remember { mutableStateOf("") }
    var estilo by remember { mutableStateOf("") }
    var kilometros by remember { mutableStateOf("") }
    var uriImagen by remember { mutableStateOf<android.net.Uri?>(null) }
    var esPrincipal by remember { mutableStateOf(false) }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uriImagen = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                        tint = Color.White
                    )
                }
                Text(
                    text = "Nueva Motocicleta",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White,
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(TorqueColors.SurfaceDark)
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
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = TorqueColors.OnSurfaceVariant, modifier = Modifier.size(48.dp))
                            Text("Añadir Foto", color = TorqueColors.OnSurfaceVariant)
                        }
                    }
                }

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

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Establecer como principal", color = Color.White, modifier = Modifier.weight(1f))
                        Switch(
                            checked = esPrincipal,
                            onCheckedChange = { esPrincipal = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TorqueColors.Primary)
                        )
                    }

                    TorqueTextField(
                        value = estilo,
                        onValueChange = { estilo = it },
                        label = "Notas",
                        singleLine = false,
                        modifier = Modifier.height(100.dp)
                    )
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            TorqueButton(
                text = "Guardar Moto",
                icon = Icons.Default.Save,
                onClick = {
                    if (marca.isBlank() || modelo.isBlank()) {
                        Toast.makeText(contexto, "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show()
                        return@TorqueButton
                    }

                    scope.launch {
                        val rutaFoto = uriImagen?.let { FileUtils.copiarImagenAInterno(contexto, it) }
                        
                        // Obtenemos el conteo antes de insertar
                        val numMotos = repositorio.contarMotos()
                        val marcarPrincipal = esPrincipal || numMotos == 0
                        
                        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        
                        val nuevaMoto = MotoEntity(
                            marca = marca.trim(),
                            modelo = modelo.trim(),
                            anio = anio.toIntOrNull() ?: 0,
                            matricula = matricula.trim(),
                            kilometros = kilometros.toIntOrNull() ?: 0,
                            cilindrada = cilindrada.trim(),
                            caballos = caballos.trim().ifEmpty { null },
                            estilo = estilo.trim(),
                            esPrincipal = 0, // Insertamos inicialmente como 0
                            uriFoto = rutaFoto,
                            creadoEn = fecha,
                            actualizadoEn = fecha
                        )

                        val id = repositorio.insertarMoto(nuevaMoto)
                        
                        // Si debe ser principal, actualizamos después de insertar para asegurar el reseteo de las otras
                        if (marcarPrincipal && id > 0) {
                            repositorio.establecerMotoPrincipal(id)
                        }

                        withContext(Dispatchers.Main) {
                            Toast.makeText(contexto, "¡Moto añadida con éxito!", Toast.LENGTH_SHORT).show()
                            (contexto as? Activity)?.finish()
                        }
                    }
                }
            )
        }
    }
}