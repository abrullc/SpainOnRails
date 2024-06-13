package com.abrullc.spainonrails.details.routeDetailModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentRouteDetailBinding
import com.abrullc.spainonrails.retrofit.entities.Ruta
import com.abrullc.spainonrails.retrofit.services.RutaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteDetailFragment : Fragment() {
    private var idRuta: Int = 0
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentRouteDetailBinding

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

            val resultRuta = rutaService.getRuta(idRuta)

            withContext(Dispatchers.Main) {
                setupRutaInfo(resultRuta)
            }
        }, this, requireContext())
    }

    private fun setupRutaInfo(ruta: Ruta) {
        with(mBinding) {
            tvRouteDetailName.text = ruta.origen+" - "+ruta.destino
        }
    }
}