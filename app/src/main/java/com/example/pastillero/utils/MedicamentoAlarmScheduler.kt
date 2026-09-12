package com.example.pastillero.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pastillero.data.Medicamento
import com.example.pastillero.receiver.MedicamentoAlarmReceiver
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Gestor encargado de programar y cancelar alarmas exactas en el servicio [AlarmManager] del sistema Android.
 *
 * @param context Contexto de la aplicación.
 */
class MedicamentoAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Programa una alarma en el sistema para la hora exacta configurada en la entidad [Medicamento].
     *
     * @param medicamento El medicamento que se va a programar.
     * @throws SecurityException Captura el fallo en Android 12+ si el permiso de alarmas exactas no está concedido,
     * realizando un plan de contingencia (fallback) con una alarma no exacta.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun programarAlarma(medicamento: Medicamento) {
        val intent = Intent(context, MedicamentoAlarmReceiver::class.java).apply {
            putExtra("EXTRA_NOMBRE", medicamento.nombre)
            putExtra("EXTRA_DOSIS", medicamento.dosis)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val ahora = LocalDateTime.now()
        var fechaHoraAlarma = LocalDateTime.of(
            ahora.toLocalDate(),
            medicamento.horaExacta
        )

        if (fechaHoraAlarma.isBefore(ahora)) {
            fechaHoraAlarma = fechaHoraAlarma.plusDays(1)
        }

        val tiempoMillis = fechaHoraAlarma.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                tiempoMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Manejo de excepción de seguridad: si no hay permiso de alarma exacta, se programa una alarma estándar
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                tiempoMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancela la alarma previamente programada para un medicamento en el sistema.
     *
     * @param medicamento El medicamento cuya alarma se desea cancelar.
     */
    fun cancelarAlarma(medicamento: Medicamento) {
        val intent = Intent(context, MedicamentoAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
