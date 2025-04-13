package com.example.torque.ui.theme.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.torque.MaintenanceItem


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

@Composable
fun ButtonMantenimiento() {

    val checkedState = remember { mutableStateOf(false) }
    Switch(
        checked = checkedState.value, onCheckedChange = { checkedState.value = it })
}

@Composable
fun MaintenanceItemCard(item: MaintenanceItem) {

    val context = LocalContext.current
    var checked by remember { mutableStateOf(false) }

    Button(
        onClick = {
            // Acción al hacer click en la card
        },
        modifier = Modifier
            .padding(8.dp)
            .width(320.dp)
            .height(100.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.LightGray,
                    checkedTrackColor = Color(0xFF4CAF50),     // verde activado
                    uncheckedTrackColor = Color(0xFFBDBDBD)    // gris desactivado
                )
            )
        }
    }
}





