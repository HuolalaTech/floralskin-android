package com.xlcx.floralskin.core.handle

import android.view.View
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import com.xlcx.floralskin.core.ISkinPadding
import com.xlcx.floralskin.core.SkinManager


/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class HandleOfPadding(val view: View) : ISkinPadding {

    private var padding: Int? = null
    private var paddingLeft: Int? = null
    private var paddingRight: Int? = null
    private var paddingTop: Int? = null
    private var paddingBottom: Int? = null

    override fun setSkinPadding(id: Int) {
        padding = id
        view.setPadding(getDimen(id).toInt())
    }

    override fun setSkinPaddingLeft(id: Int) {
        paddingLeft = id
        view.updatePadding(left = getDimen(id).toInt())
    }

    override fun setSkinPaddingRight(id: Int) {
        paddingRight = id
        view.updatePadding(right = getDimen(id).toInt())
    }

    override fun setSkinPaddingTop(id: Int) {
        paddingTop = id
        view.updatePadding(top = getDimen(id).toInt())
    }

    override fun setSkinPaddingBottom(id: Int) {
        paddingBottom = id
        view.updatePadding(bottom = getDimen(id).toInt())
    }

    fun getDimen(id: Int) = SkinManager.getDimens(id)

    fun updatePadding() {
        padding?.let { setSkinPadding(it) }
        paddingLeft?.let { setSkinPaddingLeft(it) }
        paddingRight?.let { setSkinPaddingRight(it) }
        paddingTop?.let { setSkinPaddingTop(it) }
        paddingBottom?.let { setSkinPaddingBottom(it) }
    }
}
