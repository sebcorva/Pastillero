package com.example.pastillero.data.repository

import com.example.pastillero.data.User
import com.example.pastillero.data.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Pruebas unitarias automatizadas para [UserRepository] utilizando [JUnit] y [Mockito].
 *
 * Simula el comportamiento del DAO de usuarios ([UserDao]).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        // Inicialización tardía con Mockito
        userDao = mock(UserDao::class.java)
        userRepository = UserRepository(userDao)
    }

    /**
     * Verifica que [UserRepository.getUserByEmail] devuelva el usuario correspondiente cuando existe en el DAO.
     */
    @Test
    fun getUserByEmail_retornaUsuario_cuandoExiste() = runTest {
        val email = "juan@gmail.com"
        val expectedUser = User(id = 1, nombre = "Juan Pérez", email = email, password = "hashPassword123")
        `when`(userDao.getUserByEmail(email)).thenReturn(expectedUser)

        val result = userRepository.getUserByEmail(email)

        assertEquals(expectedUser, result)
        verify(userDao).getUserByEmail(email)
    }

    /**
     * Verifica que [UserRepository.getUserByEmail] devuelva null cuando el usuario no existe en la base de datos.
     */
    @Test
    fun getUserByEmail_retornaNull_cuandoNoExiste() = runTest {
        val email = "noexiste@gmail.com"
        `when`(userDao.getUserByEmail(email)).thenReturn(null)

        val result = userRepository.getUserByEmail(email)

        assertNull(result)
        verify(userDao).getUserByEmail(email)
    }

    /**
     * Verifica que [UserRepository.insertUser] delegue correctamente la inserción al [UserDao].
     */
    @Test
    fun insertUser_llamaADaoCorrectamente() = runTest {
        val newUser = User(nombre = "María López", email = "maria@gmail.com", password = "hashPassword456")

        userRepository.insertUser(newUser)

        verify(userDao).insertUser(newUser)
    }

    /**
     * Verifica que [UserRepository.updatePassword] delegue la actualización de clave al [UserDao].
     */
    @Test
    fun updatePassword_llamaADaoCorrectamente() = runTest {
        val email = "juan@gmail.com"
        val newPassword = "newHashPassword789"

        userRepository.updatePassword(email, newPassword)

        verify(userDao).updatePassword(email, newPassword)
    }
}
