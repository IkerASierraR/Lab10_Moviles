package com.example.permisossierra.fetures

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

object CallHelper {

    fun callNumber(context: Context, phoneNumber: String): String {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Permiso CALL_PHONE no otorgado"
        }

        val cleanNumber = phoneNumber.filter { it.isDigit() || it == '+' }
        if (cleanNumber.isBlank()) {
            return "Ingresa un número de teléfono válido"
        }

        return try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$cleanNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            "Llamada iniciada al número:\n $cleanNumber"
        } catch (e: SecurityException) {
            "Error de seguridad: ${e.localizedMessage}"
        } catch (e: ActivityNotFoundException) {
            "No se encontró una aplicación de teléfono en el dispositivo"
        } catch (e: Exception) {
            "Error al realizar la llamada: ${e.localizedMessage}"
        }
    }
}
