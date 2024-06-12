package com.abrullc.spainonrails.mainModule.routesModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.FragmentRoutesBinding
import com.abrullc.spainonrails.retrofit.services.RutaService
import com.abrullc.spainonrails.mainModule.routesModule.adapters.RoutesListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoutesFragment : Fragment() {
    private lateinit var mBinding: FragmentRoutesBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var mRouteAdapter: RoutesListAdapter
    private lateinit var mRouteLinearLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRoutesBinding.inflate(inflater, container, false)

        commonFunctions = CommonFunctions()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mRouteAdapter = RoutesListAdapter()

        mRouteLinearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        with(mBinding) {
            rcvRoutes.apply {
                setHasFixedSize(true)
                layoutManager = mRouteLinearLayoutManager
                adapter = mRouteAdapter
            }
        }

        getRutaes()
    }

    private fun getRutaes() {
        commonFunctions.launchLifeCycleScope({
            val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)

            val resultRutaes = rutaService.getRutas().body()!!

            withContext(Dispatchers.Main) {
                mRouteAdapter.submitList(resultRutaes)
            }
        }, this, requireContext())
    }
}