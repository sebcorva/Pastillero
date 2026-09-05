package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

class RecuperarViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun verificarEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank()) {
            onError("Ingresa un correo electrónico")
            return
        }

        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                onSuccess()
            } else {
                onError("No tienes una cuenta con este correo. Créala primero.")
            }
        }
    }

    fun cambiarPassword(
        email: String,
        nuevaPassword: String,
        repetirPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nuevaPassword.isBlank() || nuevaPassword != repetirPassword) {
            onError("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            val nuevaPasswordHash = PasswordHasher.hash(nuevaPassword)
            userRepository.updatePassword(email, nuevaPasswordHash)
            onSuccess()
        }
    }
}
