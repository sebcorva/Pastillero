package com.example.pastillero.data.repository

import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.MedicamentoDao
import kotlinx.coroutines.flow.Flow

class MedicamentoRepository(private val medicamentoDao: MedicamentoDao) {
    val allMedicamentos: Flow<List<Medicamento>> = medicamentoDao.getAllMedicamentos()

    suspend fun insertMedicamento(medicamento: Medicamento) {
        medicamentoDao.insertMedicamento(medicamento)
    }

    suspend fun updateMedicamento(medicamento: Medicamento) {
        medicamentoDao.updateMedicamento(medicamento)
    }

    suspend fun deleteMedicamento(medicamento: Medicamento) {
        medicamentoDao.deleteMedicamento(medicamento)
    }
}
