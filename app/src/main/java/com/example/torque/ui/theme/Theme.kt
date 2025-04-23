package com.example.torque.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Paleta personalizada con negros, amarillos y azules
val Black = Color(0xFF000000)
val DarkGray = Color(0xFF1A1A1A)
val Yellow = Color(0xFFFFC107)
val DarkYellow = Color(0xFFFFA000)
val LightYellow = Color(0xFFFFECB3)
val Blue = Color(0xFF2196F3)
val DarkBlue = Color(0xFF1976D2)
val LightBlue = Color(0xFFBBDEFB)

private val ColoresOscuros = darkColorScheme(
    primary = Yellow,
    secondary = Blue,
    tertiary = LightBlue,
    background = Black,
    surface = DarkGray,
    onPrimary = Black,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val ColoresClaros = lightColorScheme(
    primary = DarkBlue,
    secondary = DarkYellow,
    tertiary = LightYellow,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun TorqueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactiva dynamicColor para usar siempre tu paleta
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ColoresOscuros else ColoresClaros

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Aquí puedes aplicar tu tipografía si ya la tienes
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
