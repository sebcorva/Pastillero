package com.example.pastillero

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pastillero.ui.screens.LoginScreen
import com.example.pastillero.ui.screens.MainScreen
import com.example.pastillero.ui.screens.RecuperarScreen
import com.example.pastillero.ui.screens.RegistroScreen
import com.example.pastillero.ui.theme.PastilleroTheme
import com.example.pastillero.ui.viewmodel.AppViewModelFactory
import com.example.pastillero.utils.SessionManager

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PastilleroTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val factory = remember { AppViewModelFactory(context) }
                val sessionManager = remember { SessionManager(context) }

                // Definir destino inicial dinámico basándose en si la sesión está activa en SharedPreferences
                val startDestination = if (sessionManager.estaLogueado()) "principal" else "login"
                
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(
                            viewModel = viewModel(factory = factory),
                            onNavegarAPrincipal = {
                                navController.navigate("principal") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavegarARegistro = { navController.navigate("registro") },
                            onNavegarARecuperar = { navController.navigate("recuperar") }
                        )
                    }
                    composable("registro") {
                        RegistroScreen(
                            viewModel = viewModel(factory = factory),
                            onNavegarALogin = { navController.popBackStack() }
                        )
                    }
                    composable("recuperar") {
                        RecuperarScreen(
                            viewModel = viewModel(factory = factory),
                            onNavegarALogin = { navController.popBackStack() }
                        )
                    }
                    composable("principal") {
                        MainScreen(
                            viewModel = viewModel(factory = factory),
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("principal") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
