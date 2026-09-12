package com.example.pastillero.utils

import android.content.Context
import android.widget.Toast
import com.example.pastillero.data.Medicamento

/**
 * Función de extensión para la clase [Context] de Android.
 *
 * Muestra un mensaje flotante (Toast) en pantalla de forma simplificada, reduciendo el código verborragico.
 *
 * @param mensaje Texto que se desea mostrar en el Toast.
 * @param duracion Duración del mensaje en pantalla ([Toast.LENGTH_SHORT] por defecto).
 */
fun Context.mostrarToast(mensaje: String, duracion: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, mensaje, duracion).show()
}

/**
 * Propiedad de extensión calculada para la clase [Medicamento].
 *
 * Devuelve un texto descriptivo del estado visual del medicamento.
 *
 * @return "Tomado ✔" si el remedio ya se registró como ingerido, o "Pendiente ⏰" en caso contrario.
 */
val Medicamento.estadoTexto: String
    get() = if (tomado) "Tomado ✔" else "Pendiente ⏰"
