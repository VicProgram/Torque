package com.example.torque.garage

import android.app.Activity
import android.net.Uri
import android.os.Bundle
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
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AgregarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                AgregarMotoView()
            }
        }
    }
}

@Composable
fun AgregarMotoView() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anno by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var colorMoto by remember { mutableStateOf("") }
    var cilindrada by remember { mutableStateOf("") }
    var cv by remember { mutableStateOf("") }
    var estilo by remember { mutableStateOf("") }
    var kms by remember { mutableStateOf("") }
    var fechaCompra by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
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
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TorqueColors.OnSurface
                    )
                }
                Text(
                    text = "Add Motorcycle",
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
                // --- Photo Selector ---
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
                                if (imageUri == null) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = TorqueColors.Outline,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                } else Modifier
                            )
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
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
                                    "No photo selected",
                                    color = TorqueColors.OnSurfaceVariant,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Add Photo Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, TorqueColors.Outline, CircleShape)
                            .clickable { imagePicker.launch("image/*") }
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
                                "Add Photo",
                                color = TorqueColors.Primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // --- Input Fields ---
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TorqueTextField(marca, { marca = it }, "Brand *")
                    TorqueTextField(modelo, { modelo = it }, "Model *")

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(anno, { anno = it }, "Year", modifier = Modifier.weight(1f))
                        TorqueTextField(matricula, { matricula = it }, "Plate", modifier = Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(kms, { kms = it }, "Current Kms", modifier = Modifier.weight(1f))
                        TorqueTextField(cilindrada, { cilindrada = it }, "CC", modifier = Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TorqueTextField(cv, { cv = it }, "CV", modifier = Modifier.weight(1f))
                        TorqueTextField(colorMoto, { colorMoto = it }, "Color", modifier = Modifier.weight(1f))
                    }

                    TorqueTextField(
                        value = estilo,
                        onValueChange = { estilo = it },
                        label = "Optional Notes",
                        singleLine = false,
                        modifier = Modifier.height(120.dp)
                    )
                }

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
                text = "Save Motorcycle",
                icon = Icons.Default.Save,
                onClick = {
                    if (marca.isBlank() || modelo.isBlank()) {
                        showToast(context, "Please fill required fields")
                        return@TorqueButton
                    }

                    val dbHelper = TorqueDatabaseHelper(context)
                    
                    val moto = Moto(
                        marca = marca.trim(),
                        modelo = modelo.trim(),
                        anno = anno.toIntOrNull() ?: 0,
                        matricula = matricula.trim(),
                        colorMoto = colorMoto.trim(),
                        cilindrada = cilindrada.trim(),
                        cv = cv.trim(),
                        estilo = estilo.trim(),
                        kms = kms.toIntOrNull() ?: 0,
                        fechaCompra = fechaCompra.trim(),
                        esPrincipal = if (dbHelper.getMotos().isEmpty()) 1 else 0,
                        fotoUri = imageUri?.toString()
                    )

                    if (dbHelper.addMoto(moto)) {
                        showToast(context, "Motorcycle saved successfully!")
                        (context as? Activity)?.apply {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    } else {
                        showToast(context, "Error saving motorcycle")
                    }
                }
            )
        }
    }
}