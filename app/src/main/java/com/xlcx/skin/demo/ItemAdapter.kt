package com.xlcx.skin.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xlcx.skin.demo.databinding.ItemViewBindingImpl

/**
 * author : simon
 * time   : 2024/08/26
 * desc   :
 */
class ItemAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val description: TextView

        init {
            title = view.findViewById(R.id.title)
            description = view.findViewById(R.id.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemViewBindingImpl>(
            LayoutInflater.from(parent.context), R.layout.item_view, parent, false
        )

        return ViewHolder(binding.root)

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.title.text = item.getTitle()
        holder.description.text = item.getDescription()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}