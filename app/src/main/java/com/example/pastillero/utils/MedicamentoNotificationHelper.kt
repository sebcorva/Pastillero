package com.example.pastillero.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Objeto auxiliar para la gestión de canales de notificaciones y patrones de vibración táctil.
 */
object MedicamentoNotificationHelper {
    private const val CHANNEL_ID = "medicamentos_channel_id"
    private const val CHANNEL_NAME = "Recordatorios de Medicamentos"
    private const val CHANNEL_DESC = "Notificaciones visibles y vibración para la hora del medicamento"

    // Patrón de vibración: 2 seguidos + 1 posterior
    // [espera=0ms, vib1=300ms, pausa=150ms, vib2=300ms, pausa=500ms, vib3=700ms]
    val patronVibracion = longArrayOf(0, 300, 150, 300, 500, 700)

    /**
     * Crea el canal de notificación de alta prioridad requerido en Android 8.0 (API 26) o superior.
     *
     * @param context Contexto de la aplicación.
     */
    fun crearCanalNotificacion(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                vibrationPattern = patronVibracion
                enableLights(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Construye y despliega la notificación flotante de alta prioridad en pantalla.
     *
     * @param context Contexto de ejecución.
     * @param nombreMedicamento Nombre del remedio a mostrar en el título.
     * @param dosis Cantidad/Dosis del remedio a mostrar en el contenido.
     * @throws SecurityException Captura el fallo de seguridad en caso de que los permisos de notificación hayan sido revocados en Android 13+.
     */
    fun mostrarNotificacionMedicamento(context: Context, nombreMedicamento: String, dosis: String) {
        crearCanalNotificacion(context)

        // Ejecutar vibración directa para asegurar el patrón táctil
        ejecutarVibracion(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("⏰ Hora de tu medicamento")
            .setContentText("Es hora de tomar: $nombreMedicamento ($dosis)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVibrate(patronVibracion)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: SecurityException) {
            // Manejo de excepción de seguridad si no hay permisos de notificación
            e.printStackTrace()
        }
    }

    /**
     * Ejecuta directamente el patrón de vibración (2 pulsos seguidos + 1 posterior) en el motor háptico del teléfono.
     *
     * @param context Contexto del sistema.
     * @throws Exception Captura cualquier fallo genérico de hardware o de servicio de vibración no disponible.
     */
    fun ejecutarVibracion(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createWaveform(patronVibracion, -1))
                }
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createWaveform(patronVibracion, -1))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(patronVibracion, -1)
                    }
                }
            }
        } catch (e: Exception) {
            // Manejo de excepción genérica para evitar cierres si el hardware de vibración falla
            e.printStackTrace()
        }
    }
}
