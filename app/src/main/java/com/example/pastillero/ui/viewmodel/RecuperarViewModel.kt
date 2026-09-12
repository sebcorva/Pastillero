package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.repository.UserRepository
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la verificación de cuenta y actualización de contraseña en la recuperación de clave.
 *
 * @property userRepository Repositorio de usuarios.
 */
class RecuperarViewModel(private val userRepository: UserRepository) : ViewModel() {
    /**
     * Comprueba si el correo ingresado corresponde a un usuario registrado.
     *
     * @param email Correo a consultar.
     * @param onSuccess Callback ejecutado si la cuenta existe.
     * @param onError Callback ejecutado si la cuenta no existe o el campo está vacío.
     */
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

    /**
     * Valida la nueva contraseña, calcula su hash SHA-256 y actualiza el registro del usuario en la base de datos.
     *
     * @param email Correo electrónico de la cuenta.
     * @param nuevaPassword Nueva clave en texto plano.
     * @param repetirPassword Confirmación de la nueva clave.
     * @param onSuccess Callback ejecutado al actualizar la contraseña.
     * @param onError Callback que notifica inconsistencias entre las contraseñas.
     */
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
