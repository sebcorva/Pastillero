package com.example.pastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.repository.MedicamentoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val medicamentoRepository: MedicamentoRepository) : ViewModel() {
    val listaMedicamentos: StateFlow<List<Medicamento>> =
        medicamentoRepository.allMedicamentos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarMedicamentos(medicamentos: List<Medicamento>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            medicamentos.forEach { medicamentoRepository.insertMedicamento(it) }
            onSuccess()
        }
    }

    fun eliminarMedicamento(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.deleteMedicamento(medicamento)
        }
    }
}
