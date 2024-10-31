package com.xlcx.floralskin.core

import android.graphics.drawable.Drawable
import androidx.annotation.UiThread
import androidx.collection.ArraySet

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
private const val DAY = "day"
private const val NIGHT = "night"

object DayNightController {

    private val mNotify = ArraySet<IDayNightModeChange>()

    private var currentMode: String = DAY

    fun isDayMode(): Boolean {
        return currentMode == DAY
    }

    @UiThread
    fun changeMode() {
        val result =
            if (isDayMode()) {
                NIGHT
            } else {
                DAY
            }
        currentMode = result
        notifyModeChange()
    }

    fun register(notify: IDayNightModeChange) {
        if (mNotify.contains(notify)) {
            mNotify.remove(notify)
        }
        mNotify.add(notify)
    }

    @Synchronized
    fun unregister(notify: IDayNightModeChange) {
        mNotify.remove(notify)
    }


    private fun notifyModeChange() {
        mNotify.forEach { it.change(isDayMode()) }
    }
}

/**
 * getSkinDrawable
 * @param resId
 * @return
 */
fun getSkinDrawable(resId: Int): Drawable {
    return SkinManager.getDrawable(resId)
}

/**
 * getSkinMipmap
 * @param resId
 * @return
 */
fun getSkinMipmap(resId: Int): Drawable {
    return SkinManager.getMipmap(resId)
}

/**
 * getSkinColor
 * @param resId
 * @return
 */
fun getSkinColor(resId: Int): Int {
    return SkinManager.getColor(resId)
}
