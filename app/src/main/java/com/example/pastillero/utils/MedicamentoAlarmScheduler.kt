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

class MedicamentoAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                tiempoMillis,
                pendingIntent
            )
        }
    }

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
