package com.example.pastillero.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pastillero.data.AppDatabase
import com.example.pastillero.data.repository.MedicamentoRepository
import com.example.pastillero.data.repository.UserRepository

class AppViewModelFactory(context: Context) : ViewModelProvider.Factory {

    //Uso de 'by lazy' para crear en memoria database, userRepository y medicamentoRepository cuando se solicite por primera vez un ViewModel
    private val database by lazy { AppDatabase.getDatabase(context) }
    private val userRepository by lazy { UserRepository(database.userDao()) }
    private val medicamentoRepository by lazy { MedicamentoRepository(database.medicamentoDao()) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegistroViewModel::class.java) -> {
                RegistroViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RecuperarViewModel::class.java) -> {
                RecuperarViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(medicamentoRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel clase no reconocida: ${modelClass.name}")
        }
    }
}
