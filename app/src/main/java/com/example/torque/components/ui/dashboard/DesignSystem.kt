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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
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
import com.example.torque.ui.theme.TorqueAccent
import com.example.torque.ui.theme.TorqueBackground
import com.example.torque.ui.theme.TorqueBorderDark
import com.example.torque.ui.theme.TorqueOnPrimary
import com.example.torque.ui.theme.TorqueOnPrimaryContainer
import com.example.torque.ui.theme.TorqueOnSecondary
import com.example.torque.ui.theme.TorqueOnSecondaryContainer
import com.example.torque.ui.theme.TorqueOnSurface
import com.example.torque.ui.theme.TorqueOnSurfaceVariant
import com.example.torque.ui.theme.TorqueOnTertiary
import com.example.torque.ui.theme.TorqueOutline
import com.example.torque.ui.theme.TorquePrimary
import com.example.torque.ui.theme.TorquePrimaryContainer
import com.example.torque.ui.theme.TorqueSecondary
import com.example.torque.ui.theme.TorqueSecondaryContainer
import com.example.torque.ui.theme.TorqueSlate500
import com.example.torque.ui.theme.TorqueSlate900
import com.example.torque.ui.theme.TorqueSurface
import com.example.torque.ui.theme.TorqueSurfaceContainer
import com.example.torque.ui.theme.TorqueSurfaceContainerHigh
import com.example.torque.ui.theme.TorqueSurfaceContainerLow
import com.example.torque.ui.theme.TorqueSurfaceDark
import com.example.torque.ui.theme.TorqueSurfaceVariant
import com.example.torque.ui.theme.TorqueTertiary

// Object para acceder a los colores desde otros archivos
object TorqueColors {
    val Primary = TorquePrimary
    val Accent = TorqueAccent
    val OnPrimary = TorqueOnPrimary
    val PrimaryContainer = TorquePrimaryContainer
    val OnPrimaryContainer = TorqueOnPrimaryContainer
    val Secondary = TorqueSecondary
    val OnSecondary = TorqueOnSecondary
    val SecondaryContainer = TorqueSecondaryContainer
    val OnSecondaryContainer = TorqueOnSecondaryContainer
    val Surface = TorqueSurface
    val OnSurface = TorqueOnSurface
    val SurfaceVariant = TorqueSurfaceVariant
    val OnSurfaceVariant = TorqueOnSurfaceVariant
    val Outline = TorqueOutline
    val Background = TorqueBackground
    val SurfaceDark = TorqueSurfaceDark
    val BorderDark = TorqueBorderDark
    val Tertiary = TorqueTertiary
    val OnTertiary = TorqueOnTertiary
    val SurfaceContainerHigh = TorqueSurfaceContainerHigh
    val SurfaceContainer = TorqueSurfaceContainer
    val SurfaceContainerLow = TorqueSurfaceContainerLow
    val Slate500 = TorqueSlate500
    val Slate900 = TorqueSlate900
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 1f - alpha))
        )
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
            Text(
                text.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            )
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
    val contexto = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(TorqueColors.Surface.copy(alpha = 0.85f))
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("Atrás", Icons.AutoMirrored.Filled.ArrowBack, false) {
                (contexto as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
            }
            BottomNavItem("Inicio", Icons.Default.GridView, true) {
                // Ya estamos en una actividad que suele tener el NavHost o es el Dashboard
            }
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { onClick() }
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