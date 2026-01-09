package com.example.torque.components

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// --- Navigation Wrappers ---
fun navigateTo(context: Context, destination: Class<*>, extras: (Intent.() -> Unit)? = null) {
    val intent = Intent(context, destination)
    extras?.invoke(intent)
    context.startActivity(intent)
}

fun launchActivity(launcher: ActivityResultLauncher<Intent>, context: Context, destination: Class<*>, extras: (Intent.() -> Unit)? = null) {
    val intent = Intent(context, destination)
    extras?.invoke(intent)
    launcher.launch(intent)
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
        if (alpha < 1f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 1f - alpha))
            )
        }
    }
}

@Composable
fun TorqueTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textStyle = TextStyle(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Cyan,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Cyan
        )
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
