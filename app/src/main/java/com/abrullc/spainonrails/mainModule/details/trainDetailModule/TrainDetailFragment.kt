package com.abrullc.spainonrails.mainModule.details.trainDetailModule

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
import com.abrullc.spainonrails.databinding.FragmentTrainDetailBinding
import com.abrullc.spainonrails.mainModule.details.routeDetailModule.RouteDetailFragment
import com.abrullc.spainonrails.mainModule.routesModule.adapters.RoutesListAdapter
import com.abrullc.spainonrails.retrofit.entities.Tren
import com.abrullc.spainonrails.retrofit.services.TrenService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrainDetailFragment : Fragment(), OnClickListener {
    private var idTren: Int = 0
    private lateinit var tren: Tren
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mBinding: FragmentTrainDetailBinding
    private lateinit var mRouteAdapter: RoutesListAdapter
    private lateinit var mRouteLinearLayoutManager: RecyclerView.LayoutManager

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

            val resultTren = trenService.getTren(idTren).body()!!

            withContext(Dispatchers.Main) {
                tren = resultTren
                setupTrenInfo()
            }
        }, this, requireContext())
    }

    private fun setupTrenInfo() {
        with(mBinding) {
            Glide.with(this@TrainDetailFragment)
                .load(tren.imagen)
                .placeholder(R.drawable.ic_train)
                .error(R.drawable.ic_train)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgTrain)

            tvTrainDetailTitle.text = tren.nombre
            tvTrainDetailCapacity.text = tren.capacidad.toString()
            tvTrainDetailDescription.text = tren.descripcion
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mRouteAdapter = RoutesListAdapter(this)

        mRouteLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvRoutesTrain.apply {
                setHasFixedSize(false)
                layoutManager = mRouteLinearLayoutManager
                adapter = mRouteAdapter
            }
        }

        getRutasTren()
    }

    private fun getRutasTren() {
        commonFunctions.launchLifeCycleScope({
            val trenService = SpainOnRailsApplication.retrofit.create(TrenService::class.java)

            val resultRutasTren = trenService.getRutasTren(tren.id).body()!!

            withContext(Dispatchers.Main) {
                mRouteAdapter.submitList(resultRutasTren)
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