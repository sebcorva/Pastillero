package com.example.pastillero.data.repository

import com.example.pastillero.data.FormatoMedicamento
import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.MedicamentoDao
import com.example.pastillero.data.MomentoDia
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalTime

/**
 * Pruebas unitarias automatizadas para [MedicamentoRepository] utilizando [JUnit] y [Mockito].
 *
 * Simula el comportamiento de [MedicamentoDao] y flujos [kotlinx.coroutines.flow.Flow] sin requerir SQLite físico.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MedicamentoRepositoryTest {

    private lateinit var medicamentoDao: MedicamentoDao
    private lateinit var medicamentoRepository: MedicamentoRepository

    @Before
    fun setup() {
        medicamentoDao = mock(MedicamentoDao::class.java)
        medicamentoRepository = MedicamentoRepository(medicamentoDao)
    }

    /**
     * Verifica que [MedicamentoRepository.allMedicamentos] emita correctamente la lista enviada por el [MedicamentoDao].
     */
    @Test
    fun allMedicamentos_emiteListaCorrecta() = runTest {
        val expectedList = listOf(
            Medicamento(
                id = 1L,
                nombre = "Paracetamol",
                dosis = "500 mg",
                formato = FormatoMedicamento.PASTILLA,
                momentoDia = MomentoDia.MANANA,
                horaExacta = LocalTime.of(8, 0)
            )
        )
        `when`(medicamentoDao.getAllMedicamentos()).thenReturn(flowOf(expectedList))

        val result = medicamentoRepository.allMedicamentos.first()

        assertEquals(expectedList, result)
        verify(medicamentoDao).getAllMedicamentos()
    }

    /**
     * Verifica que [MedicamentoRepository.insertMedicamento] delegue la inserción al [MedicamentoDao].
     */
    @Test
    fun insertMedicamento_llamaADaoCorrectamente() = runTest {
        val medicamento = Medicamento(
            nombre = "Ibuprofeno",
            dosis = "10 ml",
            formato = FormatoMedicamento.JARABE,
            momentoDia = MomentoDia.TARDE,
            horaExacta = LocalTime.of(14, 0)
        )

        medicamentoRepository.insertMedicamento(medicamento)

        verify(medicamentoDao).insertMedicamento(medicamento)
    }

    /**
     * Verifica que [MedicamentoRepository.updateMedicamento] delegue la actualización al [MedicamentoDao].
     */
    @Test
    fun updateMedicamento_llamaADaoCorrectamente() = runTest {
        val medicamento = Medicamento(
            id = 2L,
            nombre = "Insulina",
            dosis = "5 UI",
            formato = FormatoMedicamento.INYECCION,
            momentoDia = MomentoDia.NOCHE,
            horaExacta = LocalTime.of(20, 0),
            tomado = true
        )

        medicamentoRepository.updateMedicamento(medicamento)

        verify(medicamentoDao).updateMedicamento(medicamento)
    }

    /**
     * Verifica que [MedicamentoRepository.deleteMedicamento] delegue la eliminación al [MedicamentoDao].
     */
    @Test
    fun deleteMedicamento_llamaADaoCorrectamente() = runTest {
        val medicamento = Medicamento(
            id = 3L,
            nombre = "Amoxicilina",
            dosis = "500 mg",
            formato = FormatoMedicamento.PASTILLA,
            momentoDia = MomentoDia.MANANA,
            horaExacta = LocalTime.of(8, 0)
        )

        medicamentoRepository.deleteMedicamento(medicamento)

        verify(medicamentoDao).deleteMedicamento(medicamento)
    }
}
