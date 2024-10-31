package com.xlcx.floralskin.core.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.xlcx.floralskin.core.handle.HandleOfDayNightBg
import com.xlcx.floralskin.core.handle.HandleOfPadding
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.IDayNightMember
import com.xlcx.floralskin.core.IDayNightModeChange
import com.xlcx.floralskin.core.IDayNightNotify
import com.xlcx.floralskin.core.ISkinPadding

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class DayNightRelativeLayout :
    RelativeLayout,
    IDayNightModeChange,
    IDayNightNotify,
    IDayNightMember,
    ISkinPadding {
    private val handle = HandleOfDayNightBg(this)
    private val mPadding = HandleOfPadding(this)
    private var hasSkinPadding = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0 - 1)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(context, attrs, defStyleAttr, -1)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        DayNightController.register(this)
    }

    override fun setDayNight(isDay: Boolean?) {
        // xml与注册的选择其一即可
        if (isDay != null) {
            DayNightController.unregister(this)
        }
        handle.setDayNight(isDay)
        if (hasSkinPadding) {
            mPadding.updatePadding()
        }
    }

    override fun change(isDay: Boolean) {
        handle.setDayNight(isDay)
        if (hasSkinPadding) {
            mPadding.updatePadding()
        }
    }
    override fun setDayResource(res: Int) {
        handle.setDayResource(res)
    }

    override fun setNightResource(res: Int) {
        handle.setNightResource(res)
    }
    override fun setSkinPadding(id: Int) {
        hasSkinPadding = true
        mPadding.setSkinPadding(id)
    }

    override fun setSkinPaddingLeft(id: Int) {
        hasSkinPadding = true
        mPadding.setSkinPaddingLeft(id)
    }

    override fun setSkinPaddingRight(id: Int) {
        hasSkinPadding = true
        mPadding.setSkinPaddingRight(id)
    }

    override fun setSkinPaddingTop(id: Int) {
        hasSkinPadding = true
        mPadding.setSkinPaddingTop(id)
    }

    override fun setSkinPaddingBottom(id: Int) {
        hasSkinPadding = true
        mPadding.setSkinPaddingBottom(id)
    }
}
