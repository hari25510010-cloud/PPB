package com.example.todoku.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoku.MainActivity2
import com.example.todoku.databinding.ItemMainactivity2Binding
import com.example.todoku.entity.text

class TodoAdapter(
    private val dataset: MutableList<text>,
    private val todoItemEvent: TodoAdapter.TodoItemEvent

) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    interface TodoItemEvent {
        fun onTodoItemEdit(todo: text): Unit
        fun onTodoItemDelete(todo: text): Unit

    }

    inner class CustomViewHolder(val view: ItemMainactivity2Binding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: text) {
            view.title.text = data.title
            view.description.text = data.description
            //eh kotlin berikan saya notifikasi ketika element root(RelativeLayout) itu di klik
            view.root.setOnClickListener {
                //kode disini adalah aksi kita setelah mendapatkan notifikasi sebuah element di klik
                todoItemEvent.onTodoItemEdit(data)
            }
            //eh kotlin berikan saya notifikasi ketika element root(RelativeLayout) itu di klik dan di tahan sekian detik
            view.root.setOnClickListener {
                todoItemEvent.onTodoItemDelete(data)
                true
            }
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
