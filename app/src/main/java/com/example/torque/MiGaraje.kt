package com.example.torque


import Moto
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.torque.database.MiGarajeDatabaseHelper
import com.example.torque.ui.theme.componentes.BotonCuadrado

class MiGaraje : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MigarajeView()
        }
    }
}

@Composable
fun MigarajeView() {
    val context = LocalContext.current
    val dbHelper = MiGarajeDatabaseHelper(context)

    val motos = remember { mutableStateListOf<Moto>() }

    LaunchedEffect(true) {
        motos.clear()
        motos.addAll(dbHelper.obtenerMotos())  // Usamos dbHelper aquí
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondorevwebp),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura encima de la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Contenido centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mi Garaje",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Crear los botones con las motos en dos columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(motos.size) { index ->
                    val moto = motos[index]
                    BotonCuadrado(
                        texto = "${moto.Marca} ${moto.Modelo}", onClick = {
                            val intent = Intent(context, MotoDetalle::class.java)
                            intent.putExtra("idMoto", moto.idMoto)  // Pasamos el id de la moto
                            context.startActivity(intent)
                        }, modifier = Modifier
                            .width(150.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}



