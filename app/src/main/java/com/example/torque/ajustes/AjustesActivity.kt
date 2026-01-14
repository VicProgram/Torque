package com.example.torque.ajustes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torque.components.ui.dashboard.TorqueColors
import com.example.torque.ui.theme.TorqueTheme

class AjustesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                AjustesScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun AjustesScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TorqueColors.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Header ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }
                Text(
                    text = "Ajustes",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "PREFERENCIAS",
                    color = TorqueColors.Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                AjusteToggleItem(
                    title = "Notificaciones",
                    subtitle = "Alertas de mantenimiento",
                    icon = Icons.Default.Notifications
                )

                AjusteToggleItem(
                    title = "Modo Oscuro",
                    subtitle = "Interfaz de alto contraste",
                    icon = Icons.Default.Palette,
                    initialValue = true
                )

                HorizontalDivider(
                    color = TorqueColors.SurfaceVariant.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "SISTEMA",
                    color = TorqueColors.Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                AjusteSimpleItem(
                    title = "Privacidad",
                    subtitle = "Gestionar tus datos",
                    icon = Icons.Default.Shield
                )

                AjusteSimpleItem(
                    title = "Copia de Seguridad",
                    subtitle = "Backup de la base de datos",
                    icon = Icons.Default.Storage
                )
            }
        }
    }
}

@Composable
fun AjusteToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    initialValue: Boolean = false
) {
    var checked by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TorqueColors.SurfaceDark)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = TorqueColors.OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(text = subtitle, color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = TorqueColors.Primary,
                checkedTrackColor = TorqueColors.Primary.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun AjusteSimpleItem(title: String, subtitle: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TorqueColors.SurfaceDark)
            .clickable { /* Action */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = TorqueColors.OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(text = subtitle, color = TorqueColors.OnSurfaceVariant, fontSize = 12.sp)
        }
    }
}
