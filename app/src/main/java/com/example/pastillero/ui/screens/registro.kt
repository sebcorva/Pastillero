package com.example.pastillero.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pastillero.ui.viewmodel.AppViewModelFactory
import com.example.pastillero.ui.viewmodel.RegistroViewModel
import com.example.pastillero.utils.mostrarToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavegarALogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Crear Cuenta", 
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
                text = "Regístrate",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { 
                    Text(
                        "Nombre", 
                        style = MaterialTheme.typography.bodyLarge 
                    ) 
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { 
                    Text(
                        "Contraseña", 
                        style = MaterialTheme.typography.bodyLarge 
                    ) 
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.registrarUsuario(
                        nombre = nombre,
                        email = email,
                        password = password,
                        onSuccess = {
                            context.mostrarToast("Registro exitoso")
                            onNavegarALogin()
                        },
                        onError = { mensaje -> context.mostrarToast(mensaje) }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    "Registrarse",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TextButton(
                onClick = onNavegarALogin,
                modifier = Modifier.heightIn(min = 64.dp)
            ) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
