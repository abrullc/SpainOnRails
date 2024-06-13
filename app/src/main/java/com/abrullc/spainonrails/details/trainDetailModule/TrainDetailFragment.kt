package com.abrullc.spainonrails.details.trainDetailModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentTrainDetailBinding
import com.abrullc.spainonrails.retrofit.entities.Tren
import com.abrullc.spainonrails.retrofit.services.TrenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrainDetailFragment : Fragment() {
    private var idTren: Int = 0
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentTrainDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            idTren = it.getInt("idTren")
        }

        commonFunctions = CommonFunctions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTrainDetailBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTren()
    }

    private fun getTren() {
        commonFunctions.launchLifeCycleScope({
            val trenService = SpainOnRailsApplication.retrofit.create(TrenService::class.java)

            val resultTren = trenService.getTren(idTren)

            withContext(Dispatchers.Main) {
                setupTrenInfo(resultTren)
            }
        }, this, requireContext())
    }

    private fun setupTrenInfo(tren: Tren) {
        with(mBinding) {
            tvTrainDetailName.text = tren.nombre
        }
    }
}