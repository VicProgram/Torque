package com.example.torque.ui.theme.componentes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BotonCuadrado(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier.size(64.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(texto)
    }
}

@Composable
fun BotonLargo(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(width = 240.dp, height = 48.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(texto)
    }
}

@Composable
fun BotonCircular(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(56.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(texto)
    }
}



