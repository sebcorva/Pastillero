package com.example.pastillero.utils

import java.security.MessageDigest

/**
 * Objeto auxiliar de seguridad encargado del Hashing de contraseñas mediante SHA-256.
 */
object PasswordHasher {
    /**
     * Calcula la firma Hash SHA-256 de una cadena de caracteres dada.
     *
     * @param password Contraseña o texto a encriptar.
     * @return Cadena de 64 caracteres en formato Hexadecimal correspondiente al Hash SHA-256.
     */
    fun hash(password: String): String {
        if (password.isEmpty()) return ""
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
