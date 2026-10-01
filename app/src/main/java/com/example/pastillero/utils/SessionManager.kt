package com.example.pastillero.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor de sesión persistente del usuario utilizando [SharedPreferences] de Android.
 *
 * Mantiene el estado de autenticación y los datos del usuario activo.
 *
 * @param context Contexto de la aplicación.
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pastillero_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
    }

    /**
     * Guarda la sesión activa del usuario en [SharedPreferences].
     *
     * @param email Correo electrónico del usuario autenticado.
     */
    fun guardarSesion(email: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    /**
     * Comprueba si el usuario tiene una sesión activa registrada.
     *
     * @return true si la sesión está activa, false en caso contrario.
     */
    fun estaLogueado(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Obtiene el correo del usuario almacenado en la sesión actual.
     *
     * @return El correo electrónico del usuario o cadena vacía si no existe.
     */
    fun obtenerEmailUsuario(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    /**
     * Elimina la sesión activa del usuario borrando todas las claves almacenadas en [SharedPreferences].
     */
    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}
