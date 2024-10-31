package com.xlcx.skin.demo


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.floralskin.core.ISkinChangeListener
import com.xlcx.floralskin.core.SkinManager
import java.io.File


class MainActivity : AppCompatActivity(), OnClickListener, ISkinChangeListener {
    private val TAG = "MainActivity"
    private lateinit var fragmentTransaction: FragmentTransaction

    private lateinit var indexFragment: FragmentIndex
    private lateinit var mineFragment: FragmentMine

    private var btnTask: Button? = null
    private var btnMine: Button? = null
    lateinit var cont: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cont = this
        SkinManager.attach(this)
        setContentView(R.layout.activity_main)
        initView()
        initShowFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        SkinManager.detach(this)
    }


    private fun initShowFragment() {
        Log.i(TAG, "initFragment: ")
        fragmentTransaction = supportFragmentManager.beginTransaction()
        indexFragment = FragmentIndex()
        mineFragment = FragmentMine()
        fragmentTransaction.add(
            R.id.fragment_up,
            indexFragment
        )
        fragmentTransaction.add(
            R.id.fragment_up,
            mineFragment
        )
        fragmentTransaction.hide(mineFragment)
        fragmentTransaction.show(indexFragment)
        fragmentTransaction.commit()
    }

    private fun initView() {
        Log.i(TAG, "initView: ")
        btnTask = findViewById(R.id.bt_1)
        btnMine = findViewById(R.id.bt_2)
        btnTask?.setOnClickListener(this)
        btnMine?.setOnClickListener(this)

        loadSkin()
        showBotnDrawable(true)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_1 -> {
                fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(mineFragment)
                fragmentTransaction.show(indexFragment)
                fragmentTransaction.commit()
                showBotnDrawable(true)
            }

            R.id.bt_2 -> {
                fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(indexFragment)
                fragmentTransaction.show(mineFragment)
                fragmentTransaction.commit()
                showBotnDrawable(false)
            }
        }

    }

    private fun showBotnDrawable(isIndex: Boolean) {
        if (isIndex) {
            val drawable: Drawable? = getDrawable(R.drawable.skin_main_tab_index_select)
            drawable?.setBounds(0, 0, 26, 26)
            btnTask?.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

            val drawable2: Drawable? = getDrawable(R.drawable.skin_main_tab_mine_unselect)
            drawable2?.setBounds(0, 0, 26, 26)
            btnMine?.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null)
        } else {
            val drawable: Drawable? = getDrawable(R.drawable.skin_main_tab_index_unselect)
            drawable?.setBounds(0, 0, 26, 26)
            btnTask?.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

            val drawable2: Drawable? = getDrawable(R.drawable.skin_main_tab_mine_select)
            drawable2?.setBounds(0, 0, 26, 26)
            btnMine?.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null)
        }
    }

    override fun onChange() {

    }

    private fun loadSkin() {
        val sp1 = getSharedPreferences(USER_SIN_SP, MODE_PRIVATE)
        val userSkin = sp1.getBoolean(USER_SIN_KEY, false)

        if (userSkin) {
            val file = File(this.cacheDir, FILE_NAME)
            if (!file.exists()) {
                Log.d("loadSkin", "nofile")
                Toast.makeText(this, "检查皮肤文件skinred-debug.zip是否存在", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            Log.d("loadSkin", file.absolutePath)
            SkinManager.enableSkin()
            SkinManager.loadSkin(PACK_NAME, file.absolutePath) {
                onLoadError {
                    Log.d("loadSkin", "onLoadError")
                }
                onLoadSuccess {
                    Log.d("loadSkin", "success")
                    getSharedPreferences(USER_SIN_SP, MODE_PRIVATE).edit()
                        .putBoolean(USER_SIN_KEY, true).apply()

                    DayNightController.changeMode()
                }
            }
        }
    }
}