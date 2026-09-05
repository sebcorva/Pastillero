package com.example.pastillero.utils

import java.security.MessageDigest

object PasswordHasher {
    fun hash(password: String): String {
        if (password.isEmpty()) return ""
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
