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

@Entity(tableName = "medicamentos")
data class Medicamento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val dosis: String = "",
    val momentoDia: MomentoDia,
    val horaExacta: LocalTime,
    val tomado: Boolean = false
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerHoraFormateada(): String {
        val formato = DateTimeFormatter.ofPattern("hh:mm a")
        return horaExacta.format(formato)
    }
}
