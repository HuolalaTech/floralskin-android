package com.xlcx.floralskin.core

import android.content.res.Resources
import java.io.File

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class XlSkin private constructor(val pkgName: String?, val filePath: String?) {

    companion object {

        fun makeSkin(path:String, packName:String?): XlSkin {
            skinLog("makeSkin", "path=$path,name=${packName}")
            return XlSkin(packName, path)
        }
    }

    /**
     * 是否是有效的皮肤
     * @return
     */
    fun isWorkSkin(): Boolean {
        if (pkgName.isNullOrEmpty() || filePath.isNullOrEmpty()) {
            return false
        }
        val file = File(filePath)
        return file.exists()
    }

    override fun toString(): String {
        return "XlSkin(pkgName=$pkgName, filePath=$filePath)"
    }
}

/**
 * 当前正在展示的skin对象和资源
 * @property resources
 * @property skin
 */
class XLCurrentSkin(val resources: Resources, val skin: XlSkin)
