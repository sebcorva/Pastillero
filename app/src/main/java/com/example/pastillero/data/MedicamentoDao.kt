package com.example.pastillero.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Uso de interface para declarar las acciones que se realizaran sin especificar como se realizara
@Dao
interface MedicamentoDao {
    @Query("SELECT * FROM medicamentos")
    fun getAllMedicamentos(): Flow<List<Medicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicamento(medicamento: Medicamento)

    @Update
    suspend fun updateMedicamento(medicamento: Medicamento)

    @Delete
    suspend fun deleteMedicamento(medicamento: Medicamento)
}
