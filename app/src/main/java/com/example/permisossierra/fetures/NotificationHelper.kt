package com.example.permisossierra.fetures

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {

    private const val CHANNEL_ID = "memo_reminders"
    private const val CHANNEL_NAME = "Recordatorios de Notas"
    private var notificationId = 1000

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Recordatorios generados desde Voice & Context Memo"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun sendMemoReminder(context: Context, memoText: String): String {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Permiso POST_NOTIFICATIONS no otorgado"
        }

        return try {
            createChannel(context)

            val content = memoText.ifBlank { "Tienes una nota pendiente por revisar" }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Recordatorio de Nota")
                .setContentText(content)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            notificationId++
            NotificationManagerCompat.from(context).notify(notificationId, notification)

            "Notificación #$notificationId enviada\n Contenido: $content"
        } catch (e: SecurityException) {
            "Error de seguridad: ${e.localizedMessage}"
        } catch (e: Exception) {
            "Error al enviar la notificación: ${e.localizedMessage}"
        }
    }
}
