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

/**
 * ViewModel encargado del estado reactivo y operaciones de negocio para la pantalla principal del pastillero ([com.example.pastillero.ui.screens.MainScreen]).
 *
 * @property medicamentoRepository Repositorio encargado del acceso a datos de medicamentos.
 */
class MainViewModel(private val medicamentoRepository: MedicamentoRepository) : ViewModel() {
    /**
     * Flujo de estado ([StateFlow]) que expone la lista de medicamentos actualizados en tiempo real.
     */
    val listaMedicamentos: StateFlow<List<Medicamento>> =
        medicamentoRepository.allMedicamentos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Guarda una lista de medicamentos en la base de datos y programa sus alarmas correspondientes en [MedicamentoAlarmScheduler].
     *
     * @param context Contexto requerido para programar la alarma.
     * @param medicamentos Lista de [Medicamento] a registrar.
     * @param onSuccess Callback ejecutado al completar el guardado.
     */
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

    /**
     * Conmuta el estado de confirmación visual (tomado / no tomado) de un medicamento y actualiza Room.
     *
     * @param medicamento Instancia de [Medicamento] a actualizar.
     */
    fun alternarEstadoTomado(medicamento: Medicamento) {
        viewModelScope.launch {
            val medicamentoActualizado = medicamento.copy(tomado = !medicamento.tomado)
            medicamentoRepository.updateMedicamento(medicamentoActualizado)
        }
    }

    /**
     * Dispara inmediatamente la prueba de notificación visible y patrón de vibración táctil.
     *
     * @param context Contexto de la aplicación.
     * @param medicamento Medicamento opcional para personalizar el mensaje de prueba.
     */
    fun probarNotificacionYVibracion(context: Context, medicamento: Medicamento? = null) {
        MedicamentoNotificationHelper.mostrarNotificacionMedicamento(
            context,
            "Prueba de Vibración",
            "Esto es una vibración de prueba"
        )
    }

    /**
     * Cancela la alarma del medicamento en [MedicamentoAlarmScheduler] y lo elimina de la base de datos.
     *
     * @param context Contexto de ejecución.
     * @param medicamento Instancia de [Medicamento] a eliminar.
     */
    fun eliminarMedicamento(context: Context, medicamento: Medicamento) {
        val scheduler = MedicamentoAlarmScheduler(context)
        scheduler.cancelarAlarma(medicamento)
        viewModelScope.launch {
            medicamentoRepository.deleteMedicamento(medicamento)
        }
    }
}
