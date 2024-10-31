package com.xlcx.floralskin.core

/**
 *     author : simon
 *     time   : 2024/07/22
 *     desc   :
 */
import androidx.annotation.ColorRes


/** 对于不方便从xml中得知是否是夜间模式的时候，可以通过实现该接口来更新夜间模式的UI 它与@IDayNightNotify 二选其一即可，不需要同时使用 */
interface IDayNightModeChange {
    fun change(isDay: Boolean)
}

/** 设置夜间模式切换，针对有夜间模式的专属UI的View */
interface IDayNightNotify {
    fun setDayNight(isDay: Boolean?)
}

/** 每一种View的基础可用属性 */
interface IDayNightMember {
    fun setDayResource(res: Int)
    fun setNightResource(res: Int)
}

/** tv的高亮字体的夜间和日间的模式 */
interface IDayNightTextHighlight {
    fun setDayHighlight(@ColorRes color: Int)
    fun setNightHighlight(@ColorRes color: Int)
}

/** 需要的后续再继续增加接口，暂时只提供了drawableEnd */
interface IDayNightTvDrawable {
    fun setDayDrawableEnd(drawable: Int)
    fun setNightDrawableEnd(drawable: Int)
}

/** tv的背景设置 */
interface IDayNightTvBackground {
    fun setDayBackground(drawable: Int)
    fun setNightBackground(drawable: Int)
    fun setMemberBackground(drawable: Int)
    fun setMemberDayBackground(drawable: Int)
    fun setMemberNightBackground(drawable: Int)
}

/** 支持换肤的padding 属性 */
interface ISkinPadding {
    fun setSkinPadding(id: Int)
    fun setSkinPaddingLeft(id: Int)
    fun setSkinPaddingRight(id: Int)
    fun setSkinPaddingTop(id: Int)
    fun setSkinPaddingBottom(id: Int)
}
