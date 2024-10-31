package com.xlcx.skin.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.xlcx.floralskin.core.DayNightController
import com.xlcx.skin.demo.databinding.FragmentMineBinding

class FragmentMine : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMineBinding
    private var mContxt: Context? = null

    init {
        Log.i(TAG, "FragmentMine: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContxt = this.context
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_mine, container, false)
        binding.root.findViewById<View>(R.id.daynight_change).setOnClickListener(this)
        binding.root.findViewById<View>(R.id.setting_icon).setOnClickListener(this)
        return binding.root
    }


    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "FragmentMine onDetach: ")
    }

    companion object {
        private const val TAG = "FragmentMine"
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.daynight_change -> {
                    DayNightController.changeMode()
                }

                R.id.setting_icon -> {
                    val intent = Intent(mContxt, SettinActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
