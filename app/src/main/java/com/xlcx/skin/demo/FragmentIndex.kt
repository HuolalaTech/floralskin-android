package com.xlcx.skin.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xlcx.floralskin.core.widget.DayNightRecyclerView
import com.xlcx.skin.demo.databinding.FragmentIndexBinding

class FragmentIndex : Fragment() {
    private lateinit var rView: DayNightRecyclerView
    private lateinit var binding: FragmentIndexBinding
    init {
        Log.i(TAG, "down01Fragment: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_index, container, false)
        rView = binding.root.findViewById(R.id.recyclerView)
        initRecyleView()
        return binding.root
    }

    private fun initRecyleView() {
        rView.layoutManager = LinearLayoutManager(this.context)
        val itemsList: MutableList<Item> = mutableListOf()

        for (i in 1..20) {
            var item = Item(i, "Task $i", "Description of Task $i")
            itemsList.add(item)
        }
        var adapter =  ItemAdapter(itemsList)
        rView.adapter = adapter
    }


    companion object {
        private const val TAG = "TaskListFragment"
    }
}
