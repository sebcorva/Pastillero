package com.example.pastillero.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Objeto de Acceso a Datos (DAO) para la entidad [Medicamento].
 *
 * Define las operaciones CRUD sobre la tabla 'medicamentos' en Room.
 */
@Dao
interface MedicamentoDao {
    /**
     * Obtiene el flujo reactivo de la lista completa de medicamentos registrados en la base de datos.
     *
     * @return [Flow] que emite la lista de [Medicamento] cada vez que hay cambios en la tabla.
     */
    @Query("SELECT * FROM medicamentos")
    fun getAllMedicamentos(): Flow<List<Medicamento>>

    /**
     * Inserta un medicamento en la base de datos. Si el ID ya existe, reemplaza el registro.
     *
     * @param medicamento El objeto [Medicamento] a guardar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicamento(medicamento: Medicamento)

    /**
     * Actualiza la información de un medicamento existente en la base de datos.
     *
     * @param medicamento El objeto [Medicamento] con los datos modificados.
     */
    @Update
    suspend fun updateMedicamento(medicamento: Medicamento)

    /**
     * Elimina un medicamento específico de la base de datos.
     *
     * @param medicamento El objeto [Medicamento] a borrar.
     */
    @Delete
    suspend fun deleteMedicamento(medicamento: Medicamento)
}
