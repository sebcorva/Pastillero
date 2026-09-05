package com.example.pastillero.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class MomentoDia(val etiqueta: String) {
    MANANA("Mañana"),
    TARDE("Tarde"),
    NOCHE("Noche")
}

// POO: Enum con encapsulamiento de propiedades y comportamiento de formato de medicamento
enum class FormatoMedicamento(val etiqueta: String, val unidadMedida: String) {
    PASTILLA("Pastilla", "tableta(s)"),
    JARABE("Jarabe", "ml"),
    INYECCION("Inyección", "UI"),
    GOTAS("Gotas", "gota(s)")
}
//Uso de POO dentro de Medicamento
@Entity(tableName = "medicamentos")
data class Medicamento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val dosis: String = "",
    val formato: FormatoMedicamento = FormatoMedicamento.PASTILLA,
    val momentoDia: MomentoDia,
    val horaExacta: LocalTime,
    val tomado: Boolean = false
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerHoraFormateada(): String {
        val formatoHora = DateTimeFormatter.ofPattern("hh:mm a")
        return horaExacta.format(formatoHora)
    }

    // Encapsulamiento y Polimorfismo: Formatear la instrucción de toma según el formato del medicamento
    fun obtenerInstruccionDosis(): String {
        val dosisLimpia = dosis.trim()
        //Validacion dosis ingresada
        if (dosisLimpia.isEmpty()) return formato.etiqueta

        //Verifica formato de medicamentos y retorna dosis con unidad de medida
        return when (formato) {
            FormatoMedicamento.PASTILLA -> "$dosisLimpia ${formato.unidadMedida}"
            FormatoMedicamento.JARABE -> "$dosisLimpia ${formato.unidadMedida} (Agitar antes de usar)"
            FormatoMedicamento.INYECCION -> "$dosisLimpia ${formato.unidadMedida}"
            FormatoMedicamento.GOTAS -> "$dosisLimpia ${formato.unidadMedida}"
        }
    }

    // Encapsulamiento: Lógica de estado de dominio
    fun estaPendiente(): Boolean = !tomado
}
