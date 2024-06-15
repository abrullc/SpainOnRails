package com.abrullc.spainonrails.details.routeDetailModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.interfaces.OnClickListener
import com.abrullc.spainonrails.common.interfaces.OnTrainClickListener
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentRouteDetailBinding
import com.abrullc.spainonrails.details.stationDetailModule.StationDetailFragment
import com.abrullc.spainonrails.details.trainDetailModule.TrainDetailFragment
import com.abrullc.spainonrails.mainModule.stationsModule.adapters.StationsListAdapter
import com.abrullc.spainonrails.mainModule.trainsModule.adapters.TrainsListAdapter
import com.abrullc.spainonrails.retrofit.entities.Ruta
import com.abrullc.spainonrails.retrofit.entities.Tren
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.retrofit.services.RutaService
import com.abrullc.spainonrails.retrofit.services.TrenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteDetailFragment : Fragment(), OnClickListener, OnTrainClickListener {
    private var idRuta: Int = 0
    private lateinit var ruta: Ruta
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentRouteDetailBinding
    private lateinit var mStationAdapter: StationsListAdapter
    private lateinit var mStationLinearLayoutManager: RecyclerView.LayoutManager
    private lateinit var mTrainAdapter: TrainsListAdapter
    private lateinit var mTrainLinearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            idRuta = it.getInt("idRuta")
        }

        commonFunctions = CommonFunctions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRouteDetailBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRuta()
    }

    private fun getRuta() {
        commonFunctions.launchLifeCycleScope({
            val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)

            val resultRuta = rutaService.getRuta(idRuta).body()!!

            withContext(Dispatchers.Main) {
                setupRutaInfo(resultRuta)
            }
        }, this, requireContext())
    }

    private fun setupRutaInfo(ruta: Ruta) {
        with(mBinding) {
            tvRouteDetailStartStation.text = ruta.origen
            tvRouteDetailEndStation.text = ruta.destino
            tvRouteDetailDescription.text = ruta.descripcion
        }

        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        setupStationsRecyclerView()
        setupTrainsRecyclerView()
    }

    private fun setupStationsRecyclerView() {
        mStationAdapter = StationsListAdapter(this)

        mStationLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvStationsRoute.apply {
                setHasFixedSize(false)
                layoutManager = mStationLinearLayoutManager
                adapter = mStationAdapter
            }
        }

        getEstacionesRuta()
    }

    private fun setupTrainsRecyclerView() {
        mTrainAdapter = TrainsListAdapter(this)

        mTrainLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvTrainsRoute.apply {
                setHasFixedSize(false)
                layoutManager = mTrainLinearLayoutManager
                adapter = mTrainAdapter
            }
        }

        getTrenesRuta()
    }

    private fun getEstacionesRuta() {
        commonFunctions.launchLifeCycleScope({
            val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)

            val resultEstacionesRuta = rutaService.getEstacionesRuta(idRuta).body()!!

            withContext(Dispatchers.Main) {
                mStationAdapter.submitList(resultEstacionesRuta)
            }
        }, this, requireContext())
    }

    private fun getTrenesRuta() {
        commonFunctions.launchLifeCycleScope({
            val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)

            val resultTrenRuta = rutaService.getTrenRuta(idRuta).body()!!

            withContext(Dispatchers.Main) {
                mTrainAdapter.submitList(mutableListOf(resultTrenRuta))
            }
        }, this, requireContext())
    }

    override fun onClick(entityId: Int) {
        val fragment = StationDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("idEstacion", entityId)
            }
        }

        commonFunctions.launchFragmentfromFragment(parentFragmentManager, fragment)
    }

    override fun onTrainClick(trainId: Int) {
        val fragment = TrainDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("idTren", trainId)
            }
        }

        commonFunctions.launchFragmentfromFragment(parentFragmentManager, fragment)
    }
}