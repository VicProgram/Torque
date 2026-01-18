package com.example.torque.garage

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun copyImageToInternal(context: Context, uri: Uri): String? {
    return try {
        val input: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "motos_images")
        if (!file.exists()) file.mkdirs()
        val outFile = File(file, "img_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outFile).use { out ->
            input?.copyTo(out)
        }
        outFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}