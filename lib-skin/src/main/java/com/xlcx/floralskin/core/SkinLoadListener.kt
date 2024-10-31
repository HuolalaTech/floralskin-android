package com.xlcx.floralskin.core

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
class SkinLoadListener {
    internal var mLoadStart: (() -> Unit)? = null
    internal var mLoadError: ((String) -> Unit)? = null
    internal var mLoadSuccess: (() -> Unit)? = null

    fun onLoadStart(start: () -> Unit) {
        mLoadStart = start
    }

    fun onLoadError(error: (String) -> Unit) {
        mLoadError = error
    }

    fun onLoadSuccess(success: () -> Unit) {
        mLoadSuccess = success
    }
}

/** 变更通知 */
fun interface ISkinChangeListener {
    fun onChange()
}
