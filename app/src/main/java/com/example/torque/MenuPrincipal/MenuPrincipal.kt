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
import com.example.torque.components.ui.ui.TorqueDashboardScreen
import com.example.torque.garaje.ListaGaraje
import com.example.torque.mantenimientos.MantenimientoPreview
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
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            TorqueDashboardScreen(
                onIrGaraje = { 
                    context.startActivity(Intent(context, ListaGaraje::class.java))
                },
                onIrMantenimientos = { 
                    context.startActivity(Intent(context, MantenimientoPreview::class.java))
                },
                onIrPerfil = { /* Aún no implementado */ }
            )
        }
    }
}
