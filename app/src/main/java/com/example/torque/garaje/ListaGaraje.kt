package com.example.torque.garaje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.R
import com.example.torque.components.BackgroundImage
import com.example.torque.components.launchActivity
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.BotonCuadrado
import com.example.torque.ui.theme.BotonLargo
import com.example.torque.ui.theme.TorqueTheme

class ListaGaraje : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                ListaGarajeView()
            }
        }
    }
}

@Composable
fun ListaGarajeView() {
    val context = LocalContext.current
    val dbHelper = remember { TorqueDatabaseHelper(context) }
    val motos = remember { mutableStateListOf<Moto>() }

    fun recargarMotos() {
        motos.clear()
        motos.addAll(dbHelper.obtenerMotos())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        recargarMotos()
    }

    LaunchedEffect(Unit) {
        recargarMotos()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(R.drawable.negrobluevert, alpha = 0.97f)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp, bottom = 100.dp)
        ) {
            items(motos.size) { i ->
                val moto = motos[i]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BotonCuadrado(
                        texto = "${moto.marca} ${moto.modelo}",
                        onClick = {
                            launchActivity(launcher, context, MotoDetalle::class.java) {
                                putExtra("idMoto", moto.idMoto)
                            }
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            BotonLargo(
                modifier = Modifier.fillMaxWidth(0.8f),
                texto = "Nueva Moto",
                onClick = {
                    launchActivity(launcher, context, AgregarMoto::class.java)
                }
            )
        }
    }
}
