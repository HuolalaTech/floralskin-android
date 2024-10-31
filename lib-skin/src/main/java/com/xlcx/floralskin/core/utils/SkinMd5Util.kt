package com.xlcx.floralskin.core.utils

import com.xlcx.floralskin.core.skinLog
import com.xlcx.floralskin.core.utils.FileUtils.closeIOQuietly
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest


/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class SkinMd5Util private constructor() {
    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {
        /**
         * 获取文件的MD5值
         *
         * @param file 文件路径
         * @return md5值
         */
        fun getFileMD5(file: File?): String {
            if (!FileUtils.isFileExists(file)) {
                return ""
            }
            var fis: InputStream? = null
            try {
                val digest = MessageDigest.getInstance("MD5")
                fis = FileInputStream(file)
                val buffer = ByteArray(8192)
                var len: Int
                while (fis.read(buffer).also { len = it } != -1) {
                    digest.update(buffer, 0, len)
                }
                return bytes2Hex(digest.digest())
            } catch (e: Exception) {
                skinLog("getFileMD5", e.message ?: "")
                e.printStackTrace()
            } finally {
                if (fis != null) {
                    closeIOQuietly(fis)
                }
            }
            return ""
        }

        /**
         * 一个byte转为2个hex字符
         *
         * @param src byte数组
         * @return 16进制大写字符串
         */
        private fun bytes2Hex(src: ByteArray): String {
            val res = CharArray(src.size shl 1)
            val hexDigits = charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
            )
            var i = 0
            var j = 0
            while (i < src.size) {
                res[j++] = hexDigits[src[i].toInt() ushr 4 and 0x0F]
                res[j++] = hexDigits[src[i].toInt() and 0x0F]
                i++
            }
            return String(res)
        }
    }
}
