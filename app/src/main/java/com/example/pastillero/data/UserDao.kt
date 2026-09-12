package com.example.pastillero.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Objeto de Acceso a Datos (DAO) para la entidad [User].
 *
 * Define la interfaz de operaciones CRUD sobre la tabla 'usuarios' en la base de datos de Room.
 */
@Dao
interface UserDao {
    /**
     * Consulta un usuario en la base de datos a partir de su correo electrónico.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return Instancia de [User] si existe, o null si no se encuentra registrado.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Inserta un nuevo usuario en la base de datos de Room.
     *
     * @param user El objeto [User] a registrar.
     */
    @Insert
    suspend fun insertUser(user: User)
    
    /**
     * Actualiza la contraseña encriptada de un usuario según su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @param newPassword Nuevo hash de la contraseña.
     */
    @Query("UPDATE usuarios SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)
}
