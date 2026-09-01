package com.example.pastillero.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.repository.MedicamentoRepository
import com.example.pastillero.utils.MedicamentoAlarmScheduler
import com.example.pastillero.utils.MedicamentoNotificationHelper
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun agregarMedicamentos(context: Context, medicamentos: List<Medicamento>, onSuccess: () -> Unit) {
        val scheduler = MedicamentoAlarmScheduler(context)
        viewModelScope.launch {
            medicamentos.forEach { med ->
                medicamentoRepository.insertMedicamento(med)
                scheduler.programarAlarma(med)
            }
            onSuccess()
        }
    }

    fun alternarEstadoTomado(medicamento: Medicamento) {
        viewModelScope.launch {
            val medicamentoActualizado = medicamento.copy(tomado = !medicamento.tomado)
            medicamentoRepository.updateMedicamento(medicamentoActualizado)
        }
    }

    fun probarNotificacionYVibracion(context: Context, medicamento: Medicamento? = null) {
        MedicamentoNotificationHelper.mostrarNotificacionMedicamento(
            context,
            "Prueba de Vibración",
            "Esto es una vibración de prueba"
        )
    }

    fun eliminarMedicamento(context: Context, medicamento: Medicamento) {
        val scheduler = MedicamentoAlarmScheduler(context)
        scheduler.cancelarAlarma(medicamento)
        viewModelScope.launch {
            medicamentoRepository.deleteMedicamento(medicamento)
        }
    }
}
