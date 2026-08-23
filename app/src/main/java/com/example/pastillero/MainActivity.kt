package com.example.pastillero

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pastillero.ui.screens.LoginScreen
import com.example.pastillero.ui.screens.MainScreen
import com.example.pastillero.ui.screens.RegistroScreen
import com.example.pastillero.ui.screens.RecuperarScreen
import com.example.pastillero.ui.theme.PastilleroTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PastilleroTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
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
                        RegistroScreen(onNavegarALogin = { navController.popBackStack() })
                    }
                    composable("recuperar") {
                        RecuperarScreen(onNavegarALogin = { navController.popBackStack() })
                    }
                    composable("principal") {
                        MainScreen(
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PastilleroTheme {
        Greeting("Android")
    }
}