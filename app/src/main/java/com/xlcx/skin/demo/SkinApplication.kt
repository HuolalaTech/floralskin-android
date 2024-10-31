package com.xlcx.skin.demo

import android.app.Application
import android.util.Log
import com.xlcx.floralskin.core.SkinManager

/**
 *     author : simon
 *     time   : 2024/08/07
 *     desc   :
 */
class SkinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSkin()
    }


    private fun initSkin() {
        SkinManager.init(this, listener = {
            onLoadStart {
                Log.d("SkinApplication","onstart")
            }
            onLoadSuccess {  Log.d("SkinApplication","onLoadSuccess")}
            onLoadError { Log.d("SkinApplication","onLoadError") }
        },true)
    }
}