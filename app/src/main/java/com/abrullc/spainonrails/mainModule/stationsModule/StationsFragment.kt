package com.abrullc.spainonrails.mainModule.stationsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.interfaces.OnClickListener
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentStationsBinding
import com.abrullc.spainonrails.details.stationDetailModule.StationDetailFragment
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.mainModule.stationsModule.adapters.StationsListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationsFragment : Fragment(), OnClickListener {
    private lateinit var mBinding: FragmentStationsBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mStationAdapter: StationsListAdapter
    private lateinit var mStationLinearLayoutManager: RecyclerView.LayoutManager
    private lateinit var listaEstaciones: MutableList<Estacion>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStationsBinding.inflate(inflater, container, false)

        commonFunctions = CommonFunctions()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        mStationAdapter = StationsListAdapter(this)

        mStationLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvStations.apply {
                setHasFixedSize(true)
                layoutManager = mStationLinearLayoutManager
                adapter = mStationAdapter
            }
        }

        getEstaciones()
    }

    private fun getEstaciones() {
        commonFunctions.launchLifeCycleScope({
            val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)

            val resultEstaciones = estacionService.getEstaciones()

            withContext(Dispatchers.Main) {
                mStationAdapter.submitList(resultEstaciones.body()!!)
                listaEstaciones = resultEstaciones.body()!!
            }
        }, this, requireContext())
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
        val filteredStations = listaEstaciones.filter {
            it.nombre.contains(filter, ignoreCase = true)
        }

        mStationAdapter.submitList(filteredStations)
    }

    override fun onClick(entityId: Int) {
        val fragment = StationDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("idEstacion", entityId)
            }
        }

        commonFunctions.launchFragmentfromFragment(parentFragmentManager, fragment)
    }
}