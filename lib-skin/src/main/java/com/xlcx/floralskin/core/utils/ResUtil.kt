package com.xlcx.floralskin.core.utils


import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import com.xlcx.floralskin.core.SkinManager

/**
 *     author : simon
 *     time   : 2024/07/25
 *     desc   :
 */
object ResUtil {

    /**
     * 获取资源字符串
     *
     * @param resId 资源ID
     */
    fun getString(resId: Int): String {
        return resources().getString(resId)
    }


    /**
     * 获取资源颜色
     *
     * @param resId 资源ID
     */
    fun getColor(resId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources().getColor(resId, SkinManager.appContext.theme)
        } else {
            resources().getColor(resId)
        }
    }

    /**
     * 获取资源drawable对象
     *
     * @param resId 资源ID
     */
    fun getDrawable(resId: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resources().getDrawable(resId, SkinManager.appContext.theme)
        } else {
            resources().getDrawable(resId)
        }
    }

    fun isDrawable(resId: Int): Boolean {
        val type = resources().getResourceTypeName(resId)
        return type.equals("drawable") || type.equals("Drawable")
    }

    private fun resources(): Resources {
        return SkinManager.appContext.resources
    }

    fun drawable(name: String): Int {
        return identifier(name, "drawable")
    }

    private fun identifier(name: String, type: String): Int {
        val resources = resources()
        val packageName = SkinManager.appContext.packageName
        return resources.getIdentifier(name, type, packageName)
    }
}