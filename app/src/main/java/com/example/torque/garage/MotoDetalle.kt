package com.example.torque.garage

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.room.AppRepository
import com.example.torque.room.entity.FotoEntity
import com.example.torque.room.entity.MotoEntity
import com.example.torque.ui.theme.TorqueTheme
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMoto = intent.getLongExtra("idMoto", -1L)

        setContent {
            TorqueTheme {
                VistaMotoDetalle(idMoto)
            }
        }
    }
}

@Composable
fun VistaMotoDetalle(idMoto: Long) {
    val contexto = LocalContext.current
    val repositorio = remember { AppRepository.obtenerInstancia(contexto) }
    val scope = rememberCoroutineScope()
    
    var moto by remember { mutableStateOf<MotoEntity?>(null) }
    val fotos = remember { mutableStateListOf<FotoEntity>() }
    var mostrarMenu by remember { mutableStateOf(false) }
    val estadoScroll = rememberScrollState()

    LaunchedEffect(idMoto) {
        if (idMoto != -1L) {
            moto = repositorio.obtenerMotoPorId(idMoto)
            repositorio.obtenerFotosPorMoto(idMoto).collect { listaFotos ->
                fotos.clear()
                fotos.addAll(listaFotos)
            }
        }
    }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val ruta = copiarImagenAInterno(contexto, it)
            if (ruta != null && idMoto != -1L) {
                scope.launch {
                    val nuevaFoto = FotoEntity(
                        idMoto = idMoto,
                        ruta = ruta,
                        creadoEn = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    )
                    repositorio.insertarFoto(nuevaFoto)
                    Toast.makeText(contexto, "¡Foto añadida!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        if (moto == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TorqueColors.Primary)
            }
        } else {
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
                            tint = Color.White
                        )
                    }
                    Text(
                        text = moto!!.modelo,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (moto!!.esPrincipal == 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Principal",
                            tint = TorqueColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box {
                        IconButton(onClick = { mostrarMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = mostrarMenu,
                            onDismissRequest = { mostrarMenu = false },
                            modifier = Modifier.background(TorqueColors.SurfaceDark)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Hacer Principal", color = Color.White) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = TorqueColors.Primary
                                    )
                                },
                                onClick = {
                                    scope.launch {
                                        repositorio.establecerMotoPrincipal(idMoto)
                                        moto = repositorio.obtenerMotoPorId(idMoto)
                                        launch(kotlinx.coroutines.Dispatchers.Main) {
                                            Toast.makeText(contexto, "Moto principal actualizada", Toast.LENGTH_SHORT).show()
                                            (contexto as? Activity)?.setResult(Activity.RESULT_OK)
                                        }
                                    }
                                    mostrarMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar Moto", color = Color.Red) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                },
                                onClick = {
                                    scope.launch {
                                        repositorio.eliminarMoto(moto!!)
                                        launch(kotlinx.coroutines.Dispatchers.Main) {
                                            (contexto as? Activity)?.setResult(Activity.RESULT_OK)
                                            (contexto as? Activity)?.finish()
                                        }
                                    }
                                    mostrarMenu = false
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(estadoScroll)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // --- Galería de Imágenes ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 10f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(TorqueColors.SurfaceDark)
                            .clickable { selectorImagen.launch("image/*") }
                    ) {
                        val imagenAMostrar = if (fotos.isNotEmpty()) fotos.last().ruta else moto?.uriFoto
                        
                        if (!imagenAMostrar.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(imagenAMostrar),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint = TorqueColors.OnSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text("Sin fotos aún", color = TorqueColors.OnSurfaceVariant)
                            }
                        }
                    }

                    // --- Tarjeta de Información Básica ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(TorqueColors.SurfaceDark)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "ESPECIFICACIONES",
                            color = TorqueColors.Primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        FilaDetalle("Marca", moto!!.marca)
                        FilaDetalle("Año", moto!!.anio.toString())
                        FilaDetalle("Matrícula", moto!!.matricula ?: "N/A")
                        FilaDetalle("Cilindrada", moto!!.cilindrada ?: "N/A")
                        FilaDetalle("Potencia", if (moto!!.caballos != null) "${moto!!.caballos} CV" else "N/A")
                        FilaDetalle("Kilómetros", "${moto!!.kilometros} km")
                        FilaDetalle("Estilo", moto!!.estilo ?: "N/A")
                    }

                    // --- Fila de Fotos ---
                    if (fotos.size > 1) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                "GALERÍA",
                                color = TorqueColors.OnSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(fotos) { foto ->
                                    Image(
                                        painter = rememberAsyncImagePainter(foto.ruta),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaDetalle(etiqueta: String, valor: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = TorqueColors.OnSurfaceVariant, fontSize = 14.sp)
        Text(
            text = valor ?: "N/A",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun copiarImagenAInterno(contexto: Context, uri: Uri): String? {
    return try {
        val flujoEntrada = contexto.contentResolver.openInputStream(uri)
        val nombreArchivo = obtenerNombreArchivo(contexto, uri) ?: "img_${System.currentTimeMillis()}.jpg"
        val archivo = File(contexto.filesDir, nombreArchivo)
        flujoEntrada?.use { entrada ->
            archivo.outputStream().use { salida ->
                entrada.copyTo(salida)
            }
        }
        archivo.absolutePath
    } catch (e: Exception) {
        null
    }
}

fun obtenerNombreArchivo(contexto: Context, uri: Uri): String? {
    var nombre: String? = null
    contexto.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val indiceNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(indiceNombre)
        }
    }
    return nombre
}