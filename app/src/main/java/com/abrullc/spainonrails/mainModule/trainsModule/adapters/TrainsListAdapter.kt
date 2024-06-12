package com.abrullc.spainonrails.mainModule.trainsModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.databinding.ItemTrainBinding
import com.abrullc.spainonrails.retrofit.entities.Tren
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class TrainsListAdapter: ListAdapter<Tren, RecyclerView.ViewHolder>(TrenDiffCallback()) {
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTrainBinding.bind(view)

        fun setListener(tren: Tren) {
            binding.root.setOnClickListener { TODO("Listener todavía por implementar") }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_train, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tren = getItem(position)

        with(holder as ViewHolder) {
            setListener(tren)

            with(binding) {
                tvTrainName.text = tren.nombre

                Glide.with(context)
                    .load(imgTrain)
                    .placeholder(R.drawable.ic_train)
                    .error(R.drawable.ic_train)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgTrain)
            }
        }
    }

    class TrenDiffCallback : DiffUtil.ItemCallback<Tren>() {
        override fun areItemsTheSame(oldItem: Tren, newItem: Tren): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tren, newItem: Tren): Boolean {
            return oldItem == newItem
        }
    }
}