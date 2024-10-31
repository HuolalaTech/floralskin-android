package com.xlcx.skin.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.SkinManager
import java.io.File


/**
 *     author : simon
 *     time   : 2024/08/22
 *     desc   :
 */

const val USER_SIN_KEY = "usingSkin"
const val USER_SIN_SP = "skinSp"
const val FILE_NAME = "skinred-debug.zip"
const val PACK_NAME = "com.xlcx.skinred"

class SettinActivity : AppCompatActivity(), OnClickListener {
    var skinName: TextView? = null
    var skinOp: Button? = null
    var hasSkin: Boolean = false
    private val REQUEST_CODE_PERMISSION = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        skinName = findViewById(R.id.skin_name)
        skinOp = findViewById(R.id.skin_op)
        val sp1 = getSharedPreferences(USER_SIN_SP, MODE_PRIVATE)
        hasSkin = sp1.getBoolean(USER_SIN_KEY, false)

        showSkininfo(hasSkin)
        skinOp?.setOnClickListener(this)
    }

    private fun showSkininfo(hasSkin: Boolean) {
        if (hasSkin) {
            skinName?.text = "正在使用皮肤： "
            skinOp?.text = "去除皮肤"
        } else {
            skinName?.text = "当未使用皮肤"
            skinOp?.text = "使用皮肤"
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.skin_op -> {
                if (hasSkin) {
                    SkinManager.disableSkin()
                    SkinManager.resetTheme()
                    getSharedPreferences(USER_SIN_SP, MODE_PRIVATE).edit()
                        .putBoolean(USER_SIN_KEY, false).commit()
                    hasSkin = false
                    showSkininfo(false)
                    DayNightController.changeMode()
                } else {
                    doSkinOption()
                }
            }
        }
    }


    fun loadSkinfile() {
        val file = File(this.cacheDir, FILE_NAME)
        if (!file.exists()) {
            Log.d("loadSkinfile", "nofile")
            Toast.makeText(this, "检查皮肤文件skinred-debug.zip是否存在", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("loadSkinfile", file.absolutePath)
        SkinManager.enableSkin()
        SkinManager.loadSkin(PACK_NAME, file.absolutePath) {
            onLoadError {
                Log.e("loadSkinfile", "onLoadError")
            }
            onLoadSuccess {
                Log.e("loadSkinfile", "success")
                getSharedPreferences(USER_SIN_SP, MODE_PRIVATE).edit()
                    .putBoolean(USER_SIN_KEY, true).apply()
                hasSkin = true
                DayNightController.changeMode()

                skinName?.text = "正在使用皮肤： "
                skinOp?.text = "去除皮肤"
            }
        }
    }


    // 检查权限并请求
    private fun doSkinOption() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        } else {
            // 权限已经被授予，可以读取SD卡
            loadSkinfile()
        }
    }

    // 处理权限请求的结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，可以读取SD卡
                loadSkinfile()
            } else {
                // 权限被用户拒绝，需要提示用户或者引导用户去设置中开启权限
                Toast.makeText(this, "需要读取存储权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

}