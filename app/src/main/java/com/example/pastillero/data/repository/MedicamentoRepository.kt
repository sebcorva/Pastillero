package com.example.pastillero.data.repository

import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.MedicamentoDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar la persistencia y operaciones de dominio de los medicamentos.
 *
 * @param medicamentoDao DAO de Room para medicamentos.
 */
class MedicamentoRepository(private val medicamentoDao: MedicamentoDao) {
    /**
     * Flujo reactivo con la lista de todos los medicamentos registrados.
     */
    val allMedicamentos: Flow<List<Medicamento>> = medicamentoDao.getAllMedicamentos()

    /**
     * Guarda un medicamento en la base de datos.
     *
     * @param medicamento Instancia de [Medicamento] a insertar.
     */
    suspend fun insertMedicamento(medicamento: Medicamento) {
        medicamentoDao.insertMedicamento(medicamento)
    }

    /**
     * Actualiza los datos de un medicamento existente.
     *
     * @param medicamento Instancia de [Medicamento] modificada.
     */
    suspend fun updateMedicamento(medicamento: Medicamento) {
        medicamentoDao.updateMedicamento(medicamento)
    }

    /**
     * Borra un medicamento de Room.
     *
     * @param medicamento Instancia de [Medicamento] a eliminar.
     */
    suspend fun deleteMedicamento(medicamento: Medicamento) {
        medicamentoDao.deleteMedicamento(medicamento)
    }
}
