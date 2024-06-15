package com.abrullc.spainonrails.mainModule.trainsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.interfaces.OnTrainClickListener
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentTrainsBinding
import com.abrullc.spainonrails.mainModule.details.trainDetailModule.TrainDetailFragment
import com.abrullc.spainonrails.retrofit.services.TrenService
import com.abrullc.spainonrails.mainModule.trainsModule.adapters.TrainsListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrainsFragment : Fragment(), OnTrainClickListener {
    private lateinit var mBinding: FragmentTrainsBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mTrainAdapter: TrainsListAdapter
    private lateinit var mTrainLinearLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTrainsBinding.inflate(inflater, container, false)

        commonFunctions = CommonFunctions()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mTrainAdapter = TrainsListAdapter(this)

        mTrainLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvTrains.apply {
                setHasFixedSize(true)
                layoutManager = mTrainLinearLayoutManager
                adapter = mTrainAdapter
            }
        }

        getTrenes()
    }

    private fun getTrenes() {
        commonFunctions.launchLifeCycleScope({
            val trenService = SpainOnRailsApplication.retrofit.create(TrenService::class.java)

            val resultTrenes = trenService.getTrenes().body()!!

            withContext(Dispatchers.Main) {
                mTrainAdapter.submitList(resultTrenes)
            }
        }, this, requireContext())
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