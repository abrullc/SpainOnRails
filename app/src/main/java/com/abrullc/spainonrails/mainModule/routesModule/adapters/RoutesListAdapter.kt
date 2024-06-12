package com.abrullc.spainonrails.mainModule.routesModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.databinding.ItemRouteBinding
import com.abrullc.spainonrails.retrofit.entities.Ruta

class RoutesListAdapter: ListAdapter<Ruta, RecyclerView.ViewHolder>(RutaDiffCallback()) {
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRouteBinding.bind(view)

        fun setListener(ruta: Ruta) {
            binding.root.setOnClickListener { TODO("Listener todavía por implementar") }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ruta = getItem(position)

        with(holder as ViewHolder) {
            setListener(ruta)

            with(binding) {
                tvStart.text = ruta.origen
                tvDestination.text = ruta.destino
            }
        }
    }

    class RutaDiffCallback : DiffUtil.ItemCallback<Ruta>() {
        override fun areItemsTheSame(oldItem: Ruta, newItem: Ruta): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ruta, newItem: Ruta): Boolean {
            return oldItem == newItem
        }
    }
}