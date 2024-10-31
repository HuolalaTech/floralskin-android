package com.xlcx.floralskin.core.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.xlcx.floralskin.core.handle.HandleOfDayNightImageView
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.IDayNightMember
import com.xlcx.floralskin.core.IDayNightModeChange
import com.xlcx.floralskin.core.IDayNightNotify

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class DayNightImageView :
    AppCompatImageView,
    IDayNightModeChange,
    IDayNightNotify,
    IDayNightMember {
    private val handle = HandleOfDayNightImageView(this)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        DayNightController.register(this)
    }

    override fun setDayNight(isDay: Boolean?) {
        // xml与注册的选择其一即可
        if (isDay != null) {
            DayNightController.unregister(this)
        }
        handle.setDayNight(isDay)
    }
    override fun change(isDay: Boolean) {
        handle.setDayNight(isDay)
    }
    override fun setDayResource(res: Int) {
        handle.setDayResource(res)
    }

    override fun setNightResource(res: Int) {
        handle.setNightResource(res)
    }
}
