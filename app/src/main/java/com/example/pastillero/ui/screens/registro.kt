package com.example.pastillero.ui.screens

import android.widget.Toast
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
import com.example.pastillero.data.AppDatabase
import com.example.pastillero.data.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(onNavegarALogin: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

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
                    if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        scope.launch {
                            val existingUser = db.userDao().getUserByEmail(email)
                            if (existingUser == null) {
                                db.userDao().insertUser(User(nombre = nombre, email = email, password = password))
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                onNavegarALogin()
                            } else {
                                Toast.makeText(context, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
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
