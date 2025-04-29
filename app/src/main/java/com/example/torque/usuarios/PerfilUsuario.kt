package com.example.torque.usuarios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.Usuario
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

class PerfilUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                // Accede a LocalContext.current DENTRO
                val context = LocalContext.current
                val databaseHelper = remember { TorqueDatabaseHelper(context) }
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                LaunchedEffect(Unit) {
                    val userId = 1
                    usuario = databaseHelper.obtenerUsuarioPorId(userId)
                }

                PerfilView(usuario = usuario)
            }
        }
    }
}

@Composable
fun PerfilView(usuario: Usuario?) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            val fotoPerfilPath = usuario?.fotoPerfil

            val painter = if (fotoPerfilPath != null && fotoPerfilPath.isNotEmpty()) {
                androidx.compose.ui.res.painterResource(id = com.example.torque.R.drawable.fotoperfildefecto)
            } else {
                androidx.compose.ui.res.painterResource(id = com.example.torque.R.drawable.fotoperfildefecto)
            }

            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (usuario != null) {
                FilaInfoPerfil(etiqueta = "Nombre:", valor = usuario.nombre)
                Spacer(modifier = Modifier.height(8.dp))
                FilaInfoPerfil(etiqueta = "Correo Electrónico:", valor = usuario.email)
            } else {
                Text(
                    text = "No se encontraron datos del usuario.",
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun FilaInfoPerfil(etiqueta: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}