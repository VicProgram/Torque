package com.example.torque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.torque.components.BackgroundImage
import com.example.torque.components.navigateTo
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.garaje.ListaGaraje
import com.example.torque.garaje.Moto
import com.example.torque.mantenimientos.MantenimientoPreview
import com.example.torque.ui.theme.BotonLargo
import com.example.torque.ui.theme.TorqueTheme
import com.example.torque.ui.theme.pepperoni
import com.example.torque.usuarios.PerfilUsuario

class MenuPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                MenuPrincipalView()
            }
        }
    }
}

@Composable
fun MenuPrincipalView() {
    val context = LocalContext.current
    val databaseHelper = remember { TorqueDatabaseHelper(context) }
    var motoPrincipal by remember { mutableStateOf<Moto?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                motoPrincipal = databaseHelper.obtenerMotoPrincipal()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        motoPrincipal = databaseHelper.obtenerMotoPrincipal()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(R.drawable.negroama)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Torqu\uE005",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = pepperoni,
                    color = Color.White,
                    fontSize = 90.dp.value.sp,
                    shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 2f)
                ),
                modifier = Modifier.padding(top = 35.dp, bottom = 5.dp)
            )

            BotonLargo(
                onClick = { navigateTo(context, ListaGaraje::class.java) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                texto = "Mi Garaje"
            )

            BotonLargo(
                onClick = { navigateTo(context, MantenimientoPreview::class.java) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                texto = "Mantenimientos"
            )

            BotonLargo(
                onClick = { navigateTo(context, PerfilUsuario::class.java) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                texto = "Mi Perfil"
            )

            motoPrincipal?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 35.dp, bottom = 5.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = it.marca,
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontSize = 50.sp),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = it.modelo,
                        style = MaterialTheme.typography.headlineLarge.copy(color = Color.White, fontSize = 50.sp)
                    )
                }
            } ?: run {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 35.dp, bottom = 25.dp)
                ) {
                    Text(
                        text = "Moto Principal:",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 60.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.headlineLarge.copy(color = Color.White, fontSize = 60.sp)
                    )
                }
            }
        }
    }
}
