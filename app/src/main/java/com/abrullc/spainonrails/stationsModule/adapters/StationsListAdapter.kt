package com.abrullc.spainonrails.stationsModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.databinding.ItemStationBinding
import com.abrullc.spainonrails.retrofit.entities.Estacion

class StationsListAdapter: ListAdapter<Estacion, RecyclerView.ViewHolder>(EstacionDiffCallback()) {
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStationBinding.bind(view)

        fun setListener(estacion: Estacion) {
            binding.root.setOnClickListener { TODO("Listener todavía por implementar") }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_station, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val estacion = getItem(position)

        with(holder as ViewHolder) {
            setListener(estacion)

            with(binding) {
                tvStationName.text = estacion.nombre
                tvStationCity.text = estacion.poblacion
            }
        }
    }

    class EstacionDiffCallback : DiffUtil.ItemCallback<Estacion>() {
        override fun areItemsTheSame(oldItem: Estacion, newItem: Estacion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Estacion, newItem: Estacion): Boolean {
            return oldItem == newItem
        }
    }
}