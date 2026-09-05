package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.User
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

class RegistroViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nombre.isBlank()) {
            onError("Ingresa tu nombre")
            return
        }

        if (email.isBlank()) {
            onError("Ingresa tu correo electrónico")
            return
        }

        if (password.isBlank()) {
            onError("La contraseña no puede estar vacía")
            return
        }

        viewModelScope.launch {
            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser == null) {
                val passwordHash = PasswordHasher.hash(password)
                userRepository.insertUser(User(nombre = nombre, email = email, password = passwordHash))
                onSuccess()
            } else {
                onError("El correo ya está registrado")
            }
        }
    }
}
