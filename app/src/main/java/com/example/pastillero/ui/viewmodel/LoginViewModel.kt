package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

/**
 * ViewModel encargado del estado y lógica de autenticación de la pantalla de Login ([com.example.pastillero.ui.screens.LoginScreen]).
 *
 * @property userRepository Repositorio de usuarios para consultar credenciales en la base de datos.
 */
class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    /**
     * Valida las credenciales ingresadas, calcula el hash SHA-256 de la clave y procesa el inicio de sesión.
     *
     * @param email Correo electrónico ingresado por el usuario.
     * @param password Contraseña ingresada en texto plano.
     * @param onSuccess Callback que se ejecuta si las credenciales son válidas.
     * @param onError Callback que recibe el mensaje descriptivo en caso de error o credenciales inválidas.
     */
    fun iniciarSesion(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            val passwordHashIngresada = PasswordHasher.hash(password)
            if (user != null && user.password == passwordHashIngresada) {
                onSuccess()
            } else {
                onError("Correo o contraseña incorrectos")
            }
        }
    }
}
