package com.xlcx.floralskin.core.handle

import androidx.appcompat.widget.AppCompatImageView
import com.xlcx.floralskin.core.getSkinDrawable
import com.xlcx.floralskin.core.getSkinMipmap

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class HandleOfDayNightImageView(private val imageView: AppCompatImageView) :
    HandleOfDayNight(imageView) {
    override fun handleResource() {
        val drawableRes = getResourceInt()
        if (drawableRes != null) {
            setImageResource(true, drawableRes)
            return
        }
        val mipmapResId = getResourceInt2()
        if (mipmapResId != null) {
            setImageResource(false, mipmapResId)
        }
    }
    private fun setImageResource(isDrawable: Boolean, resId: Int) {
        if (resId <= 0) {
            return
        }

        if (isDrawable) {
            imageView.setImageDrawable(getSkinDrawable(resId))
        } else {
            imageView.setImageDrawable(getSkinMipmap(resId))
        }
    }
}