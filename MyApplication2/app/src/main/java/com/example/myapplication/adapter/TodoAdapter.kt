package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemMainactivity2Binding
import com.example.myapplication.entity.text

class TodoAdapter(
    private val dataset: MutableList<text>

) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(val view: ItemMainactivity2Binding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: text) {
            view.title.text = data.title
            view.description.text = data.description
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CustomViewHolder {
        val binding = ItemMainactivity2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {
        val data = dataset[position]
        holder.bind(data)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataset: List<text>) {

        Log.d("tesr", "masuk sini")
        dataset.clear()
        dataset.addAll(newDataset)
        notifyDataSetChanged()
    }
}
