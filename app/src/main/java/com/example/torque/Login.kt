package com.example.torque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.torque.components.SectionHeader
import com.example.torque.components.TorqueTextField
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.ui.theme.TorqueTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                var isLoginScreen by remember { mutableStateOf(true) }
                if (isLoginScreen) {
                    LoginView(
                        onLoginSuccess = {
                            startActivity(Intent(this, MenuPrincipal::class.java))
                            finish()
                        },
                        onNavigateToRegister = { isLoginScreen = false }
                    )
                } else {
                    // Assuming RegistroUsuarioView will also be refactored similarly
                    Text("Registro View (Needs similar refactor)")
                }
            }
        }
    }
}

@Composable
fun LoginView(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    val context = LocalContext.current
    val databaseHelper = remember { TorqueDatabaseHelper(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.negroama),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SectionHeader("Iniciar Sesión")

            TorqueTextField(
                value = email,
                onValueChange = { email = it },
                label = "Nombre de Usuario o Correo Electrónico"
            )

            TorqueTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isPassword = true
            )

            if (!loginError.isNullOrEmpty()) {
                Text(
                    text = loginError!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(onClick = {
                val usuario = databaseHelper.obtenerUsuarioPorCredencial(email)
                if (usuario != null && usuario.passwordHash == password) {
                    onLoginSuccess()
                } else {
                    loginError = "Nombre de usuario o correo electrónico y/o contraseña incorrectos"
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToRegister, modifier = Modifier.fillMaxWidth()) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
