package com.xlcx.floralskin.core.utils

import android.net.Uri
import android.os.Build
import android.util.Log
import com.xlcx.floralskin.core.SkinManager
import com.xlcx.floralskin.core.skinLog
import com.xlcx.floralskin.core.skinLogFlag
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * author : simon
 * time   : 2024/07/22
 * desc   :
 */
object FileUtils {
    fun isFileExists(file: File?): Boolean {
        if (file == null) return false
        return if (file.exists()) {
            true
        } else isFileExists(file.absolutePath)
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    private fun isFileExists(filePath: String?): Boolean {
        val file = getFileByPath(filePath) ?: return false
        return if (file.exists()) {
            true
        } else isFileExistsApi29(filePath)
    }

    fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private fun isFileExistsApi29(filePath: String?): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                val uri = Uri.parse(filePath)
                val cr = SkinManager.appContext.contentResolver
                val afd = cr.openAssetFileDescriptor(uri, "r") ?: return false
                try {
                    afd.close()
                } catch (exception: IOException) {
                    skinLog("isFileExistsApi29 io" ,exception.message?:"")
                }
            } catch (e: FileNotFoundException) {
                skinLog("isFileExistsApi29 fileNotFound" ,e.message?:"")
                return false
            }
            return true
        }
        return false
    }

    fun closeIOQuietly(vararg closeables: Closeable) {
        val var1: Array<out Closeable> = closeables
        val var2 = closeables.size
        for (var3 in 0 until var2) {
            val closeable = var1[var3]
            try {
                closeable.close()
            } catch (exception: IOException) {
                skinLog("closeIOQuietly", exception.message ?: "")
            }

        }

    }
}
