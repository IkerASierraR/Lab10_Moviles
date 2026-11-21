package com.example.permisossierra.fetures

import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CalendarHelper {

    fun getNextEvent(context: Context): String {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Permiso READ_CALENDAR no otorgado"
        }

        val uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.EVENT_LOCATION
        )
        val selection = "${CalendarContract.Events.DTSTART} >= ?"
        val selectionArgs = arrayOf(System.currentTimeMillis().toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        return try {
            context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                if (cursor.moveToFirst()) {
                    val titleIndex = cursor.getColumnIndex(CalendarContract.Events.TITLE)
                    val startIndex = cursor.getColumnIndex(CalendarContract.Events.DTSTART)
                    val locationIndex =
                        cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)

                    val title = if (titleIndex >= 0) cursor.getString(titleIndex)
                        ?: "Sin título" else "Sin título"
                    val start = if (startIndex >= 0) cursor.getLong(startIndex) else 0L
                    val location = if (locationIndex >= 0) cursor.getString(locationIndex)
                        ?.takeIf { it.isNotBlank() } ?: "Sin ubicación" else "Sin ubicación"

                    val formattedDate = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date(start))

                    "Evento: $title\n Fecha: $formattedDate\n Lugar: $location"
                } else {
                    "No hay eventos próximos en el calendario"
                }
            } ?: "No se pudo acceder a la base de datos del calendario"
        } catch (e: SecurityException) {
            "Error de seguridad: ${e.localizedMessage}"
        } catch (e: Exception) {
            "Error al leer el calendario: ${e.localizedMessage}"
        }
    }
}
