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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pastillero.ui.viewmodel.AppViewModelFactory
import com.example.pastillero.ui.viewmodel.RecuperarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarScreen(
    onNavegarALogin: () -> Unit,
    viewModel: RecuperarViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    var email by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var emailVerificado by remember { mutableStateOf(false) }
    
    val context = LocalContext.current

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
                        viewModel.verificarEmail(
                            email = email,
                            onSuccess = { emailVerificado = true },
                            onError = { mensaje -> Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show() }
                        )
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
                        viewModel.cambiarPassword(
                            email = email,
                            nuevaPassword = nuevaPassword,
                            repetirPassword = repetirPassword,
                            onSuccess = {
                                Toast.makeText(context, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                                onNavegarALogin()
                            },
                            onError = { mensaje -> Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show() }
                        )
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
