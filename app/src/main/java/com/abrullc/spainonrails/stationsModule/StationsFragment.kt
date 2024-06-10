package com.abrullc.spainonrails.stationsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.databinding.FragmentStationsBinding
import com.abrullc.spainonrails.databinding.FragmentTrainsBinding
import com.abrullc.spainonrails.stationsModule.adapters.StationsListAdapter
import com.google.android.material.search.SearchView.TransitionState

class StationsFragment : Fragment() {
    private lateinit var mBinding: FragmentStationsBinding
    private lateinit var mStationAdapter: StationsListAdapter
    private lateinit var mStationLinearLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStationsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        setupSearchView()

        return mBinding.root
    }

    private fun setupRecyclerView() {
        mStationAdapter = StationsListAdapter()
        mStationAdapter.submitList(SpainOnRailsApplication.estaciones)

        mStationLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvStations.apply {
                setHasFixedSize(true)
                layoutManager = mStationLinearLayoutManager
                adapter = mStationAdapter
            }
        }
    }

    private fun setupSearchView() {
        mBinding.svStations.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    getStationsByFilter(newText)
                    return true
                }
            }
        )
    }

    private fun getStationsByFilter(filter: String) {
        val filteredStations = SpainOnRailsApplication.estaciones.filter {
            it.nombre.contains(filter, ignoreCase = true)
        }

        mStationAdapter.submitList(filteredStations)
    }
}