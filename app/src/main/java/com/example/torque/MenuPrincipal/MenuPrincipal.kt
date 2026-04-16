package com.example.torque.MenuPrincipal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.torque.components.ui.dashboard.TorqueDashboardScreen
import com.example.torque.garage.ListaGaraje
import com.example.torque.maintenance.OpcionesMantenimiento
import com.example.torque.perfil.PerfilActivity
import com.example.torque.ui.theme.TorqueTheme

class MenuPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                TorqueNavHost()
            }
        }
    }
}

@Composable
fun TorqueNavHost() {
    val navController = rememberNavController()
    val contexto = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            TorqueDashboardScreen(
                onIrGaraje = {
                    contexto.startActivity(Intent(contexto, ListaGaraje::class.java))
                },
                onIrMantenimientos = { idMoto ->
                    val intent = Intent(contexto, OpcionesMantenimiento::class.java).apply {
                        if (idMoto != null) putExtra("idMoto", idMoto)
                    }
                    contexto.startActivity(intent)
                },
                onIrPerfil = {
                    contexto.startActivity(Intent(contexto, PerfilActivity::class.java))
                }
            )
        }
    }
}