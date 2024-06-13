package com.abrullc.spainonrails.detailsModule.stationDetailModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentStationDetailBinding
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.services.EstacionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationDetailFragment : Fragment() {
    private var idEstacion: Int = 0
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentStationDetailBinding

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

        getEstacion()
    }

    private fun getEstacion() {
        commonFunctions.launchLifeCycleScope({
            val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)

            val resultEstacion = estacionService.getEstacion(idEstacion)

            withContext(Dispatchers.Main) {
                setupEstacionInfo(resultEstacion)
            }
        }, this, requireContext())
    }

    private fun setupEstacionInfo(estacion: Estacion) {
        with(mBinding) {
            tvStationDetailName.text = estacion.nombre
        }
    }
}