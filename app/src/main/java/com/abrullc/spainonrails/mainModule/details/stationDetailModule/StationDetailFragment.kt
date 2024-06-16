package com.abrullc.spainonrails.mainModule.details.stationDetailModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.interfaces.OnClickListener
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentStationDetailBinding
import com.abrullc.spainonrails.mainModule.details.routeDetailModule.RouteDetailFragment
import com.abrullc.spainonrails.mainModule.routesModule.adapters.RoutesListAdapter
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.entities.PuntoInteres
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.retrofit.services.PuntoInteresService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationDetailFragment : Fragment(), OnMapReadyCallback, OnClickListener {
    private var idEstacion: Int = 0
    private lateinit var estacion: Estacion
    private lateinit var puntosInteresEstacion: MutableList<PuntoInteres>
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentStationDetailBinding
    private var savedInstanceState: Bundle? = null
    private lateinit var mapView: MapView
    private lateinit var mRouteAdapter: RoutesListAdapter
    private lateinit var mRouteLinearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            idEstacion = it.getInt("idEstacion")
        }

        commonFunctions = CommonFunctions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStationDetailBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.savedInstanceState = savedInstanceState

        getEstacionData()
    }

    private fun getEstacionData() {
        commonFunctions.launchLifeCycleScope({
            val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)
            val puntoInteresService = SpainOnRailsApplication.retrofit.create(PuntoInteresService::class.java)

            val resultEstacion = estacionService.getEstacion(idEstacion).body()!!
            val resultPuntosInteresEstacion = puntoInteresService.getPuntosInteresEstacion(idEstacion).body()!!

            withContext(Dispatchers.Main) {
                estacion = resultEstacion
                puntosInteresEstacion = resultPuntosInteresEstacion

                setupEstacionInfo()

                mapView = mBinding.mapView
                mapView.onCreate(savedInstanceState)
                mapView.getMapAsync(this@StationDetailFragment)
            }
        }, this, requireContext())
    }

    private fun setupEstacionInfo() {
        with(mBinding) {
            Glide.with(this@StationDetailFragment)
                .load(estacion.imagen)
                .placeholder(R.drawable.ic_station)
                .error(R.drawable.ic_station)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgStation)

            tvStationDetailTitle.text = estacion.nombre
            tvStationDetailCity.text = estacion.poblacion
            tvStationDetailLocation.text = estacion.direccion

            setupRecyclerView()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val stationLatLng = LatLng(estacion.latitud.toDouble(), estacion.longitud.toDouble())
        googleMap.addMarker(
            MarkerOptions()
                .position(stationLatLng)
                .title(estacion.nombre)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )

        for (puntoInteresEstacion in puntosInteresEstacion) {
            val latLng = LatLng(puntoInteresEstacion.latitud.toDouble(), puntoInteresEstacion.longitud.toDouble())
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(puntoInteresEstacion.nombre)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            )
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stationLatLng, 12.0f))
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = false
    }

    private fun setupRecyclerView() {
        mRouteAdapter = RoutesListAdapter(this)

        mRouteLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvRoutesStation.apply {
                setHasFixedSize(false)
                layoutManager = mRouteLinearLayoutManager
                adapter = mRouteAdapter
            }
        }

        getRutas()
    }

    private fun getRutas() {
        commonFunctions.launchLifeCycleScope({
            val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)

            val resultRutasEstacion = estacionService.getRutasEstacion(estacion.id).body()!!

            withContext(Dispatchers.Main) {
                mRouteAdapter.submitList(resultRutasEstacion)
            }
        }, this, requireContext())
    }

    override fun onClick(entityId: Int) {
        val fragment = RouteDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("idRuta", entityId)
            }
        }

        commonFunctions.launchFragmentfromFragment(parentFragmentManager, fragment)
    }
}