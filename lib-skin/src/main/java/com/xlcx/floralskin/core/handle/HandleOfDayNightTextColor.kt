package com.xlcx.floralskin.core.handle

import android.widget.TextView
import com.xlcx.floralskin.core.getSkinColor

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class HandleOfDayNightTextColor(private val target: TextView) :
    HandleOfDayNightBase(target) {

    override fun handleResource() {
        val textColor = getResourceInt() ?: return
        if (textColor <= 0) {
            return
        }
        target.setTextColor(getSkinColor(textColor))
    }
}
