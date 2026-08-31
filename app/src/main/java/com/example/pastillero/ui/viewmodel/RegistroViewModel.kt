package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.User
import com.example.pastillero.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            onError("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser == null) {
                userRepository.insertUser(User(nombre = nombre, email = email, password = password))
                onSuccess()
            } else {
                onError("El correo ya está registrado")
            }
        }
    }
}
