package com.example.torque.components.ui.dashboard

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Colors from HTML Designs (MD3 Dark + MotoKeep Accents) ---
object TorqueColors {
    val Primary = Color(0xFF135BEC) // Brand Primary
    val Accent = Color(0xFFFF6B00)  // MotoKeep Orange Accent
    val OnPrimary = Color(0xFFFFFFFF)
    
    // MD3 Palette
    val PrimaryContainer = Color(0xFF4F378B)
    val OnPrimaryContainer = Color(0xFFEADDFF)
    val Secondary = Color(0xFFCCC2DC)
    val OnSecondary = Color(0xFF332D41)
    val SecondaryContainer = Color(0xFF4A4458)
    val OnSecondaryContainer = Color(0xFFE8DEF8)
    val Surface = Color(0xFF1C1B1F)
    val OnSurface = Color(0xFFE6E1E5)
    val SurfaceVariant = Color(0xFF49454F)
    val OnSurfaceVariant = Color(0xFFCAC4D0)
    val Outline = Color(0xFF938F99)
    val Background = Color(0xFF101622) // MotoKeep Dark Background
    val SurfaceDark = Color(0xFF1C2331) // MotoKeep Surface
    val BorderDark = Color(0xFF2D3748)   // MotoKeep Border
    
    val Tertiary = Color(0xFFEFB8C8)
    val OnTertiary = Color(0xFF492532)
    val SurfaceContainerHigh = Color(0xFF2B2930)
    val SurfaceContainer = Color(0xFF211F26)
    val SurfaceContainerLow = Color(0xFF1D1B20)
    
    // Legacy / Utils
    val Slate500 = Color(0xFF64748B)
    val Slate900 = Color(0xFF0F172A)
}

// --- Navigation & Utility ---
fun navigateTo(context: Context, destination: Class<*>, extras: (Intent.() -> Unit)? = null) {
    val intent = Intent(context, destination)
    extras?.invoke(intent)
    context.startActivity(intent)
}

fun launchActivity(
    launcher: ActivityResultLauncher<Intent>,
    context: Context,
    destination: Class<*>,
    extras: (Intent.() -> Unit)? = null
) {
    val intent = Intent(context, destination)
    extras?.invoke(intent)
    launcher.launch(intent)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// --- UI Wrappers ---
@Composable
fun BackgroundImage(resourceId: Int, alpha: Float = 1f) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 1f - alpha)))
    }
}

@Composable
fun TorqueCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = TorqueColors.SurfaceDark,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp),
        content = content
    )
}

@Composable
fun TorqueTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TorqueColors.OnSurfaceVariant) },
        singleLine = singleLine,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textStyle = TextStyle(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = TorqueColors.Primary,
            unfocusedBorderColor = TorqueColors.Outline,
            cursorColor = TorqueColors.Primary,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium.copy(
            color = TorqueColors.Slate500,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        ),
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    )
}

@Composable
fun TorqueButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = TorqueColors.Primary,
    contentColor: Color = Color.White,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.size(8.dp))
            }
            Text(text.uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
fun QuickSelectChip(
    text: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) TorqueColors.Primary else TorqueColors.SurfaceVariant
    val contentColor = if (isSelected) TorqueColors.OnPrimary else TorqueColors.OnSurfaceVariant

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MotoKeepCheckItem(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TorqueColors.SurfaceDark)
            .border(1.dp, TorqueColors.BorderDark, RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!isChecked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor)
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = description, color = TorqueColors.Slate500, fontSize = 12.sp)
        }
        
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = TorqueColors.Primary,
                uncheckedColor = TorqueColors.BorderDark,
                checkmarkColor = Color.White
            ),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun DashboardBottomNav(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(TorqueColors.Surface.copy(alpha = 0.9f))
            .padding(bottom = 32.dp, top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("Inicio", Icons.Default.GridView, true)
            BottomNavItem("Stats", Icons.Default.Analytics, false)
            BottomNavItem("Rutas", Icons.Default.Explore, false)
            BottomNavItem("Ajustes", Icons.Default.Settings, false)
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: ImageVector, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { /* Navigate */ }
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) TorqueColors.SecondaryContainer else Color.Transparent)
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (isSelected) TorqueColors.OnSecondaryContainer else TorqueColors.OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = if (isSelected) Color.White else TorqueColors.OnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
