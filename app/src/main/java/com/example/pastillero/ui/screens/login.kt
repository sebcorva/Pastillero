package com.example.pastillero.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.pastillero.ui.viewmodel.LoginViewModel
import com.example.pastillero.utils.mostrarToast

/**
 * Pantalla de inicio de sesión ("LoginScreen").
 *
 * Permite al usuario ingresar sus credenciales (correo electrónico y contraseña),
 * validarlas mediante [LoginViewModel] y navegar a las pantallas de Registro, Recuperación o Principal.
 *
 * @param viewModel Instancia del [LoginViewModel] inyectada por la fábrica [AppViewModelFactory].
 * @param onNavegarAPrincipal Callback para navegar a la pantalla principal tras un inicio de sesión exitoso.
 * @param onNavegarARegistro Callback para navegar a la pantalla de creación de cuenta.
 * @param onNavegarARecuperar Callback para navegar a la pantalla de recuperación de contraseña.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    onNavegarAPrincipal: () -> Unit = {},
    onNavegarARegistro: () -> Unit = {},
    onNavegarARecuperar: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Mi Pastillero", 
                        style = MaterialTheme.typography.headlineMedium 
                    ) 
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
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { 
                    Text(
                        "Correo electrónico", 
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
                    viewModel.iniciarSesion(
                        email = email,
                        password = password,
                        onSuccess = { onNavegarAPrincipal() },
                        onError = { mensaje -> context.mostrarToast(mensaje) }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    "Iniciar Sesión", 
                    style = MaterialTheme.typography.bodyLarge 
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onNavegarARegistro,
                modifier = Modifier.heightIn(min = 64.dp)
            ) {
                Text(
                    "¿No tienes cuenta? Regístrate aquí",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            TextButton(
                onClick = onNavegarARecuperar,
                modifier = Modifier.heightIn(min = 64.dp)
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
