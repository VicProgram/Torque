package com.example.torque.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.torque.R
import com.example.torque.ui.theme.TorqueTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorqueTheme {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val repo = ProfileRepository(context.dataStore)
    val coroutineScope = rememberCoroutineScope()

    val nameFlow by repo.nameFlow.collectAsState(initial = null)
    val birthdayFlow by repo.birthdayFlow.collectAsState(initial = null)

    var name by remember { mutableStateOf(nameFlow ?: "") }
    var birthday by remember { mutableStateOf(birthdayFlow ?: "") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(id = R.string.profile_title))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.profile_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text(stringResource(id = R.string.profile_birthday_hint)) },
            placeholder = { Text(text = "YYYY-MM-DD") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            coroutineScope.launch {
                repo.setName(name)
                repo.setBirthdayIso(birthday)
            }
        }) {
            Text(text = stringResource(id = R.string.save))
        }

        // felicitación simple si hoy es cumpleaños
        val todayIso = java.time.LocalDate.now().toString()
        if (!birthday.isNullOrBlank() && birthday == todayIso) {
            Text(text = stringResource(id = R.string.happy_birthday))
        }
    }
}