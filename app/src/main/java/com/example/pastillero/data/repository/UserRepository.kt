package com.example.pastillero.data.repository

import com.example.pastillero.data.User
import com.example.pastillero.data.UserDao

/**
 * Repositorio encargado de desacoplar y centralizar las operaciones de datos relativas a usuarios.
 *
 * @param userDao DAO para interactuar con la tabla de usuarios en Room.
 */
class UserRepository(private val userDao: UserDao) {
    /**
     * Obtiene un usuario registrado por su correo electrónico.
     *
     * @param email Correo a consultar.
     * @return Instancia de [User] o null si no se encuentra.
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Instancia de [User] a insertar.
     */
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    /**
     * Actualiza la contraseña de un usuario en Room.
     *
     * @param email Correo electrónico del usuario.
     * @param newPassword Nuevo hash de la clave.
     */
    suspend fun updatePassword(email: String, newPassword: String) {
        userDao.updatePassword(email, newPassword)
    }
}
