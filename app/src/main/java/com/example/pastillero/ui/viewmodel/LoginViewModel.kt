package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
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
