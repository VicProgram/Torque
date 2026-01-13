package com.example.torque.garage

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.torque.components.ui.dashboard.TorqueButton
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.components.ui.dashboard.showToast
import com.example.torque.database.TorqueDatabaseHelper
import java.io.File

class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idMoto = intent.getIntExtra("idMoto", -1)
        val dbHelper = TorqueDatabaseHelper(this)
        val moto = if (idMoto != -1) dbHelper.getMotoById(idMoto) else null

        setContent {
            com.example.torque.ui.theme.TorqueTheme {
                MotoDetalleView(
                    moto = moto, dbHelper = dbHelper, context = this, activity = this
                )
            }
        }
    }
}

@Composable
fun MotoDetalleView(
    moto: Moto?, dbHelper: TorqueDatabaseHelper, context: Context, activity: Activity
) {
    val photos = remember { mutableStateListOf<String>() }
    var showMenu by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(moto?.idMoto) {
        moto?.idMoto?.let {
            photos.clear()
            photos.addAll(dbHelper.getPhotosByMotoId(it))
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val path = copyImageToInternal(context, it)
            if (path != null && moto != null) {
                dbHelper.insertPhoto(moto.idMoto, path)
                photos.add(path)
                showToast(context, "¡Foto añadida!")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(TorqueColors.Background)) {
        if (moto == null) {
            Text("Moto no encontrada", color = Color.White, modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // --- Header ---
                Row(
                    modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                    Text(
                        text = moto.modelo,
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menú", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(TorqueColors.SurfaceDark)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Hacer Principal", color = Color.White) },
                                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = TorqueColors.Primary) },
                                onClick = {
                                    dbHelper.setPrincipal(moto.idMoto.toString())
                                    showToast(context, "Moto principal actualizada")
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar Moto", color = Color.Red) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                                onClick = {
                                    if (dbHelper.deleteMoto(moto.idMoto.toString())) {
                                        activity.setResult(Activity.RESULT_OK)
                                        activity.finish()
                                    }
                                    showMenu = false
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f).verticalScroll(scrollState).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // --- Image Gallery ---
                    Box(
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f).clip(RoundedCornerShape(24.dp))
                            .background(TorqueColors.SurfaceDark)
                    ) {
                        if (photos.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(photos.last()),
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
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = TorqueColors.OnSurfaceVariant, modifier = Modifier.size(48.dp))
                                Text("Sin fotos aún", color = TorqueColors.OnSurfaceVariant)
                            }
                        }
                    }

                    // --- Basic Info Card ---
                    Column(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                            .background(TorqueColors.SurfaceDark).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("ESPECIFICACIONES", color = TorqueColors.Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        
                        DetailRow("Marca", moto.marca)
                        DetailRow("Año", moto.anno.toString())
                        DetailRow("Matrícula", moto.matricula)
                        DetailRow("Cilindrada", moto.cilindrada)
                        DetailRow("Potencia", "${moto.cv} CV")
                        DetailRow("Estilo", moto.estilo)
                    }

                    // --- Photos Row ---
                    if (photos.size > 1) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("GALERÍA", color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(photos) { path ->
                                    Image(
                                        painter = rememberAsyncImagePainter(path),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }
                    }

                    TorqueButton(
                        text = "Añadir Foto",
                        icon = Icons.Default.AddAPhoto,
                        onClick = { launcher.launch("image/*") },
                        color = TorqueColors.SurfaceDark,
                        contentColor = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TorqueColors.OnSurfaceVariant, fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

fun copyImageToInternal(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "bike_image_${System.currentTimeMillis()}.jpg"

        val outputFile = File(context.filesDir, fileName)
        inputStream?.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        outputFile.absolutePath
    } catch (e: Exception) {
        null
    }
}
