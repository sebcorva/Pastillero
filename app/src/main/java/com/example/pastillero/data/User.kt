package com.example.pastillero.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa a un usuario registrado en la aplicación.
 *
 * @property id Identificador único del usuario autogenerado en SQLite.
 * @property nombre Nombre completo o apodo del usuario.
 * @property email Correo electrónico usado como credencial de inicio de sesión.
 * @property password Hash SHA-256 de la contraseña almacenada de forma segura.
 */
@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String
)
