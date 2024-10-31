package com.xlcx.floralskin.core.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.xlcx.floralskin.core.handle.HandleOfDayNightTextColor
import com.xlcx.floralskin.core.handle.HandleOfPadding
import com.xlcx.floralskin.core.isFalseStrict
import com.xlcx.floralskin.core.isTrue
import com.xlcx.floralskin.core.utils.SpannerHelper
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.IDayNightMember
import com.xlcx.floralskin.core.IDayNightModeChange
import com.xlcx.floralskin.core.IDayNightNotify
import com.xlcx.floralskin.core.IDayNightTextHighlight
import com.xlcx.floralskin.core.IDayNightTvBackground
import com.xlcx.floralskin.core.IDayNightTvDrawable
import com.xlcx.floralskin.core.ISkinPadding
import com.xlcx.floralskin.core.SkinManager
import com.xlcx.floralskin.core.utils.ResUtil

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
open class DayNightTextView :
    AppCompatTextView,
    IDayNightModeChange,
    IDayNightNotify,
    IDayNightMember,
    IDayNightTextHighlight,
    IDayNightTvDrawable,
    IDayNightTvBackground,
    ISkinPadding {
    private val handle = HandleOfDayNightTextColor(this)
    // 日间的高亮
    private var dayHighLight: Int? = null
    // 夜间的高亮
    private var nightHighLight: Int? = null
    // 高亮内容
    private var highLightContext: String? = null

    // drawable end
    private var dayDrawableEnd: Int? = null
    private var nightDrawableEnd: Int? = null
    // 白天与夜间的背景
    private var dayBackground: Int? = null
    private var nightBackground: Int? = null
    private var memberBackground: Int? = null
    private var memberDayBackground: Int? = null
    private var memberNightBackground: Int? = null

    private val mPadding = HandleOfPadding(this)
    private var hasSkinPadding = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        DayNightController.register(this)
    }

    override fun change(isDay: Boolean) {
        handle.setDayNight(isDay)
        setLightContentInner()
        updateDrawableEnd()
        detectBackground()
        if (hasSkinPadding) {
            mPadding.updatePadding()
        }
    }

    override fun setDayNight(isDay: Boolean?) {
        handle.setDayNight(isDay)
        // xml 与注册只需要其一即可
        if (isDay != null) {
            DayNightController.unregister(this)
        }
        setLightContentInner()
        updateDrawableEnd()
        detectBackground()
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
    override fun setDayHighlight(color: Int) {
        dayHighLight = color
        if (highLightContext.isNullOrEmpty()) {
            return
        }
        if (handle.isDay.isTrue()) {
            setLightContentInner()
        }
    }

    override fun setNightHighlight(color: Int) {
        nightHighLight = color
        if (highLightContext.isNullOrEmpty()) {
            return
        }
        if (handle.isDay.isFalseStrict()) {
            setLightContentInner()
        }
    }

    override fun setDayDrawableEnd(drawable: Int) {
        dayDrawableEnd = drawable
        if (handle.isDay.isTrue()) {
            updateDrawableEnd()
        }
    }

    override fun setNightDrawableEnd(drawable: Int) {
        nightDrawableEnd = drawable
        if (handle.isDay.isFalseStrict()) {
            updateDrawableEnd()
        }
    }

    override fun setDayBackground(drawable: Int) {
        this.dayBackground = drawable
        if (handle.isDay.isTrue()) {
            updateBackground()
        }
    }

    override fun setNightBackground(drawable: Int) {
        this.nightBackground = drawable
        if (handle.isDay.isFalseStrict() ) {
            updateBackground()
        }
    }

    override fun setMemberBackground(drawable: Int) {
        this.memberBackground = drawable
        if (!handle.memberHasNight) {
            updateBackground()
        }
    }

    override fun setMemberDayBackground(drawable: Int) {
        this.memberDayBackground = drawable
        if (handle.memberHasNight && handle.isDay.isTrue()) {
            updateBackground()
        }
    }

    override fun setMemberNightBackground(drawable: Int) {
        this.memberNightBackground = drawable
        if (handle.memberHasNight && handle.isDay.isFalseStrict()) {
            updateBackground()
        }
    }

    fun setFakeBoldText(isFakeBoldText: Boolean) {
        paint.isFakeBoldText = isFakeBoldText
    }

    /** 更新背景 */
    private fun updateBackground() {
        val resId =
            (when {

                handle.isDay.isTrue() -> dayBackground
                handle.isDay.isFalseStrict() -> nightBackground
                else -> null
            })
                ?: return
        background = SkinManager.getDrawable(resId)
    }

    /**
     * 外部设置text调用该方法，不要使用setText
     * @param total
     * @param light
     */
    fun setLightContent(total: String?, light: String?) {
        if (total.isNullOrEmpty()) {
            return
        }
        text = total
        if (light.isNullOrEmpty()) {
            return
        }
        highLightContext = light
        setLightContentInner()
    }

    /** 加粗变色 */
    private fun setLightContentInner() {
        if (highLightContext.isNullOrEmpty()) {
            return
        }
        val isDay = handle.isDay
        val color =
            (when {
                isDay.isTrue() -> dayHighLight
                isDay.isFalseStrict() -> nightHighLight
                else -> null
            })
                ?: return
        SpannerHelper.Builder()
            .withContext(context)
            .setContent(text.toString())
            .setLightContent(highLightContext)
            .setLightColor(color)
            .setBold(true)
            .setTextView(this)
            .build()
    }

    private fun detectBackground() {
        if (handle.isDay.isTrue() && dayBackground != null) {
            updateBackground()
        } else if (handle.isDay.isFalseStrict() && nightBackground != null) {
            updateBackground()
        }
    }

    /** 不支持换肤 */
    private fun updateDrawableEnd() {
        val drawableResId =
            when {
                handle.isDay.isTrue() -> dayDrawableEnd ?: return
                handle.isDay.isFalseStrict() -> nightDrawableEnd ?: return
                else -> return
            }
        val drawable = ResUtil.getDrawable(drawableResId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        setCompoundDrawables(null, null, drawable, null)
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
