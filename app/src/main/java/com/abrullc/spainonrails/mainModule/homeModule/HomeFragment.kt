package com.abrullc.spainonrails.mainModule.homeModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentHomeBinding
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.retrofit.services.PuntoInteresService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        commonFunctions = CommonFunctions()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = mBinding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        commonFunctions.launchLifeCycleScope({
            val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)
            val puntoInteresService = SpainOnRailsApplication.retrofit.create(PuntoInteresService::class.java)

            val resultEstaciones = estacionService.getEstaciones()
            val resultPuntoInteres = puntoInteresService.getPuntosInteres()

            withContext(Dispatchers.Main) {
                val boundsBuilder = LatLngBounds.Builder()

                for (estacion in resultEstaciones.body()!!) {
                    val latLng = LatLng(estacion.latitud.toDouble(), estacion.longitud.toDouble())
                    boundsBuilder.include(latLng)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(estacion.nombre)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    )
                }

                for (puntoInteres in resultPuntoInteres.body()!!) {
                    val latLng = LatLng(puntoInteres.latitud.toDouble(), puntoInteres.longitud.toDouble())
                    boundsBuilder.include(latLng)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(puntoInteres.nombre)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    )
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 500, 500, 0))
            }
        }, this, requireContext())
    }
}