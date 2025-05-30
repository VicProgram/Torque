package com.example.torque.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.mantenimientos.MantenimientoRealizado

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
fun ButtonMantenimiento() {

    val checkedState = remember { mutableStateOf(false) }
    Switch(
        checked = checkedState.value, onCheckedChange = { checkedState.value = it })
}
@Composable
fun HistorialItemCard(item: MantenimientoRealizado) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.nombre, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha: ${item.fecha}", color = Color.Gray)
            Text(text = "Km: ${item.kilometros}", color = Color.Gray)
        }
    }
}

@Composable
fun SquareButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(180.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)), // borde cuadrado
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            lineHeight = 22.sp
        )
    }
}






