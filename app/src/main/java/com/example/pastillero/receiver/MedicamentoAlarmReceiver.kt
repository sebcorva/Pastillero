package com.example.pastillero.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pastillero.utils.MedicamentoNotificationHelper

/**
 * Receptor de Broadcast ([BroadcastReceiver]) que intercepta las alarmas programadas por [com.example.pastillero.utils.MedicamentoAlarmScheduler]
 * y desencadena la notificación visible de alta prioridad con patrón de vibración.
 */
class MedicamentoAlarmReceiver : BroadcastReceiver() {
    /**
     * Método ejecutado automáticamente por el sistema operativo cuando se activa la alarma.
     *
     * @param context Contexto de ejecución.
     * @param intent [Intent] enviado por AlarmManager con los datos extra del medicamento.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Medicamento"
        val dosis = intent.getStringExtra("EXTRA_DOSIS") ?: ""

        MedicamentoNotificationHelper.mostrarNotificacionMedicamento(context, nombre, dosis)
    }
}
