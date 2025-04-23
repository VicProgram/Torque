package com.example.torque.garaje

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.Moto
import com.example.torque.R
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

    LaunchedEffect(Unit) {
        motos.clear()
        motos.addAll(dbHelper.obtenerMotos())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.negroblue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura encima de la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.03f))
        )

        // Lista de motos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp) // Deja un espacio suficiente en la parte superior para la lista de motos
        ) {
            items(motos.size) { i ->
                val moto = motos[i]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BotonCuadrado(
                        texto = "${moto.marca} ${moto.modelo}", onClick = {
                            val intent = Intent(context, MotoDetalle::class.java)
                            intent.putExtra("idMoto", moto.idMoto)
                            context.startActivity(intent) // Aquí se inicia la actividad directamente
                        }, modifier = Modifier.width(150.dp)
                    )
                }
            }
        }

        // Caja que contiene el botón "Nueva Moto"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Ubicamos el botón en la parte inferior centrado
                .padding(32.dp) // Ajustar espacio entre el botón y el borde inferior
        ) {
            BotonLargo(
                modifier = Modifier.width(250.dp), // Ancho fijo para el botón
                texto = "Nueva Moto", onClick = {
                    val intent = Intent(context, AgregarMoto2::class.java)
                    context.startActivity(intent)

                })
        }
    }
}
