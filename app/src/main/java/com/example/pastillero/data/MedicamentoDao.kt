package com.example.pastillero.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDao {
    @Query("SELECT * FROM medicamentos")
    fun getAllMedicamentos(): Flow<List<Medicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicamento(medicamento: Medicamento)

    @Delete
    suspend fun deleteMedicamento(medicamento: Medicamento)
}
