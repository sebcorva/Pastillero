package com.example.pastillero.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pastillero.data.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarScreen(onNavegarALogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var emailVerificado by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Recuperar Cuenta", 
                        style = MaterialTheme.typography.headlineMedium 
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavegarALogin,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (!emailVerificado) "Recuperar Contraseña" else "Nueva Contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            if (!emailVerificado) {
                Text(
                    text = "Ingresa tu correo para verificar tu cuenta.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { 
                        Text(
                            "Email", 
                            style = MaterialTheme.typography.bodyLarge 
                        ) 
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        scope.launch {
                            val user = db.userDao().getUserByEmail(email)
                            if (user != null) {
                                emailVerificado = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "No tienes una cuenta con este correo. Créala primero.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        "Verificar Email",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                OutlinedTextField(
                    value = nuevaPassword,
                    onValueChange = { nuevaPassword = it },
                    label = { 
                        Text(
                            "Nueva Contraseña", 
                            style = MaterialTheme.typography.bodyLarge 
                        ) 
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = repetirPassword,
                    onValueChange = { repetirPassword = it },
                    label = { 
                        Text(
                            "Repetir Contraseña", 
                            style = MaterialTheme.typography.bodyLarge 
                        ) 
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (nuevaPassword == repetirPassword && nuevaPassword.isNotEmpty()) {
                            scope.launch {
                                db.userDao().updatePassword(email, nuevaPassword)
                                Toast.makeText(context, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                                onNavegarALogin()
                            }
                        } else {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        "Cambiar Contraseña",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            TextButton(
                onClick = onNavegarALogin,
                modifier = Modifier.heightIn(min = 64.dp)
            ) {
                Text(
                    "Volver al Login",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
