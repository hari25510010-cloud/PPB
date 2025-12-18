package com.example.latihan_api.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihan_api.databinding.ItemCatatanBinding
import com.example.latihan_api.entities.Catatan

class CatatanAdapter(
    private val dataset: MutableList<Catatan>,
    private val events: CatatanItemEvens
) : RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder>() {

    interface CatatanItemEvens {
        fun onEdit(catatan: Catatan)
    }

    inner class CatatanViewHolder(
        private val binding: ItemCatatanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setDataUI(data: Catatan) {
            binding.judul.text = data.judul
            binding.isi.text = data.isi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder {
        val binding = ItemCatatanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CatatanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        val item = dataset[position]
        holder.setDataUI(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(dataBaru: List<Catatan>) {
        dataset.clear()
        dataset.addAll(dataBaru)
        notifyDataSetChanged()
    }
}
