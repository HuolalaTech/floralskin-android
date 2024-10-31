package com.xlcx.floralskin.core.utils

import android.R
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.xlcx.floralskin.core.skinLog

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class SpannerHelper private constructor(builder: Builder) {
    private val mContent: String
    private var mTextView: TextView?
    private val mLightContents: List<String>
    private val mLightColors: List<Int>?
    private val mBolds: List<Boolean>?
    private val mClickListener: MutableList<View.OnClickListener>?
    private val mContext: Context?
    private val isSequence // 是否按顺序
            : Boolean
    var spannableString: SpannableString? = null
        private set
    var spannableStringBuilder: SpannableStringBuilder? = null
        private set

    init {
        mContent = builder.content
        mTextView = builder.textView
        mLightContents = builder.lightContents
        mClickListener = builder.clickListeners
        mLightColors = builder.lightColors
        mBolds = builder.boldList
        mContext = builder.context
        isSequence = builder.isSequence

        buildContent(builder.isSpannableStringBuilder)
    }

    private fun buildContent(isSpannableStringBuilder: Boolean) {
        if (isSpannableStringBuilder) {
            buildSpannableStringBuilder()
        } else {
            buildSpannerString()
        }
    }

    private fun buildSpannableStringBuilder() {
        val ss = SpannableStringBuilder(mContent)
        var color = 0
        var fromIndex = 0 // 记录上一个查询过的end
        var isBold = false // 是否加粗
        try {
            for (i in mLightContents.indices) {
                val startIndex = mContent.indexOf(mLightContents[i], fromIndex)
                val endIndex =
                    mContent.indexOf(mLightContents[i], fromIndex) + mLightContents[i].length
                if (isSequence) {
                    fromIndex = endIndex
                }
                if (mLightColors != null && i <= mLightColors.size - 1) {
                    color = mLightColors[i]
                }
                if (mBolds != null && i <= mBolds.size - 1) {
                    isBold = mBolds[i]
                }
                val clickSpan = ClickSpan({ view: View? ->
                    if (i <= mClickListener!!.size - 1) {
                        mClickListener[i].onClick(view)
                    }
                }, mContext!!.resources.getColor(color))
                ss.setSpan(clickSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (isBold) {
                    ss.setSpan(
                        StyleSpan(Typeface.BOLD),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                spannableStringBuilder = ss
            }
            if (mTextView != null) {
                mTextView!!.text = ss
                mTextView!!.movementMethod = LinkMovementMethod.getInstance()
                mTextView!!.highlightColor =
                    mTextView!!.context.resources.getColor(R.color.transparent)
            }
        } catch (e: Exception) {
            skinLog("buildSpannable exception" ,e.message?:"")
            e.printStackTrace()
            if (mTextView != null) {
                mTextView!!.text = mContent
            }
        }
    }

    private fun buildSpannerString() {
        val ss = SpannableString(mContent)
        var color = 0
        var fromIndex = 0 // 记录上一个查询过的end
        var isBold = false // 是否加粗
        try {
            for (i in mLightContents.indices) {
                val startIndex = mContent.indexOf(mLightContents[i], fromIndex)
                val endIndex =
                    mContent.indexOf(mLightContents[i], fromIndex) + mLightContents[i].length
                if (isSequence) {
                    fromIndex = endIndex
                }
                if (mLightColors != null && i <= mLightColors.size - 1) {
                    color = mLightColors[i]
                }
                if (mBolds != null && i <= mBolds.size - 1) {
                    isBold = mBolds[i]
                }
                val clickSpan = ClickSpan({ view: View? ->
                    if (i <= mClickListener!!.size - 1) {
                        mClickListener[i].onClick(view)
                    }
                }, mTextView!!.context.resources.getColor(color))
                ss.setSpan(clickSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (isBold) {
                    ss.setSpan(
                        StyleSpan(Typeface.BOLD),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                spannableString = ss
            }
            if (mTextView != null) {
                mTextView!!.text = ss
                mTextView!!.movementMethod = LinkMovementMethod.getInstance()
                mTextView!!.highlightColor =
                    mTextView!!.context.resources.getColor(R.color.transparent)
            }
        } catch (e: Exception) {
            skinLog("buildSpannerString Exception", e.message ?: "")
            e.printStackTrace()
            if (mTextView != null) {
                mTextView!!.text = mContent
            }
        }
    }

    fun clear() {
        mTextView = null
        mClickListener?.clear()
    }

    class Builder {
        var content = ""
        var textView: TextView? = null
        val lightContents: MutableList<String> = ArrayList()
        val lightColors: MutableList<Int> = ArrayList()
        val boldList: MutableList<Boolean> = ArrayList()
        val clickListeners: MutableList<View.OnClickListener> = ArrayList()
        var isSpannableStringBuilder = false
        var context: Context? = null
        var isSequence = false // 是否按顺序
        fun withContext(context: Context?): Builder {
            this.context = context
            return this
        }

        fun setTextView(textView: TextView?): Builder {
            this.textView = textView
            return this
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setLightContent(vararg content: String?): Builder {
            for (s in content) {
                lightContents.add(s?:"")
            }
            return this
        }

        fun setClickListener(vararg listeners: View.OnClickListener): Builder {
            for (lis in listeners) {
                clickListeners.add(lis)
            }
            return this
        }

        fun setLightColor(vararg colors: Int): Builder {
            for (color in colors) {
                lightColors.add(color)
            }
            return this
        }

        fun setBold(vararg bolds: Boolean): Builder {
            for (bold in bolds) {
                boldList.add(bold)
            }
            return this
        }

        fun setSequence(sequence: Boolean): Builder {
            isSequence = sequence
            return this
        }

        fun withSpannableStringBuilder(isSpannableStringBuilder: Boolean): Builder {
            this.isSpannableStringBuilder = isSpannableStringBuilder
            return this
        }

        fun build(): SpannerHelper {
            return SpannerHelper(this)
        }
    }

    class ClickSpan(private val mListener: View.OnClickListener, private val mColor: Int) :
        ClickableSpan(), View.OnClickListener {
        override fun onClick(v: View) {
            mListener.onClick(v)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = mColor
            ds.isUnderlineText = false //去除超链接的下划线
        }
    }
}