package com.xlcx.floralskin.core.handle

import android.view.View
import com.xlcx.floralskin.core.isFalseStrict
import com.xlcx.floralskin.core.isTrue
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.IDayNightMember
import com.xlcx.floralskin.core.IDayNightNotify

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */

/** 模式1：memberHasNight为false，有三种，即日，夜，会员 模式2：memberHasNight为true，有四种，即日，夜，会员日，会员夜 */
abstract class HandleOfDayNightBase(view: View) :
    IDayNightNotify, IDayNightMember {
    var isDay: Boolean? = null

    // 是否有日夜会员，默认无
    var memberHasNight = false

    // 日，夜，会员字体颜色
    var day: Int? = null
    var night: Int? = null
    var member: Int? = null

    init {
        if (!view.isInEditMode) {
            isDay = DayNightController.isDayMode()
        }
    }

    /** 检测是否可以刷新，避免无用的刷新 */
    open fun detect() {
        if (isDay.isTrue() && day == null) {
            return
        } else if (isDay.isFalseStrict() && night == null) {
            return
        }
        handleResource()
    }

    override fun setDayNight(isDay: Boolean?) {
        if (isDay == null) {
            return
        }
        this.isDay = isDay
        detect()
    }

    override fun setDayResource(res: Int) {
        this.day = res
        if (isDay.isTrue()) {
            handleResource()
        }
    }

    override fun setNightResource(res: Int) {
        this.night = res
        if (isDay.isFalseStrict()) {
            handleResource()
        }
    }

    fun getResourceInt(): Int? {
        return when {
            isDay.isTrue() -> {
                day
            }

            isDay.isFalseStrict() -> {
                night
            }

            else -> null
        }
    }

    /** 获取资源，告知外部 */
    abstract fun handleResource()
}
