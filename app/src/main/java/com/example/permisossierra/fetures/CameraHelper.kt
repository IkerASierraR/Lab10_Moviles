package com.example.permisossierra.fetures

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CameraHelper {

    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun describePhoto(context: Context, bitmap: Bitmap?): String {
        if (!isCameraPermissionGranted(context)) {
            return "Permiso CAMERA no otorgado"
        }
        if (bitmap == null) {
            return "Captura cancelada. No se tomó ninguna foto"
        }
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        return "Foto capturada a las $time\n Resolución: ${bitmap.width} x ${bitmap.height} px"
    }
}
