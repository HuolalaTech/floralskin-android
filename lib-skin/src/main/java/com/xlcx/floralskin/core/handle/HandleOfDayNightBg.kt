package com.xlcx.floralskin.core.handle

import android.view.View
import com.xlcx.floralskin.core.getSkinColor
import com.xlcx.floralskin.core.getSkinDrawable
import com.xlcx.floralskin.core.utils.ResUtil

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class HandleOfDayNightBg(private val target: View) : HandleOfDayNight(target) {
    override fun handleResource() {
        val resId = getResourceInt()

        if (resId != null ) {
            setBackground(ResUtil.isDrawable(resId), resId)
        }
    }

    private fun setBackground(isDrawable: Boolean, resId: Int) {
        if (resId <= 0) {
            return
        }
        if (isDrawable) {
            target.background = getSkinDrawable(resId)
        } else {
            target.setBackgroundColor(getSkinColor(resId))
        }
    }
}
