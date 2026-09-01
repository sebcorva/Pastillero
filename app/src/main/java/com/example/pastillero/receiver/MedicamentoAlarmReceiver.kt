package com.example.pastillero.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pastillero.utils.MedicamentoNotificationHelper

class MedicamentoAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Medicamento"
        val dosis = intent.getStringExtra("EXTRA_DOSIS") ?: ""

        MedicamentoNotificationHelper.mostrarNotificacionMedicamento(context, nombre, dosis)
    }
}
