package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.User
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica y validación para el registro de nuevos usuarios.
 *
 * @property userRepository Repositorio de usuarios.
 */
class RegistroViewModel(private val userRepository: UserRepository) : ViewModel() {
    /**
     * Valida los campos ingresados, encripta la contraseña e inserta el nuevo usuario si el correo no está registrado.
     *
     * @param nombre Nombre del usuario.
     * @param email Correo electrónico.
     * @param password Contraseña ingresada.
     * @param onSuccess Callback ejecutado al completar el registro con éxito.
     * @param onError Callback que notifica cualquier error de validación o duplicidad de correo.
     */
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
