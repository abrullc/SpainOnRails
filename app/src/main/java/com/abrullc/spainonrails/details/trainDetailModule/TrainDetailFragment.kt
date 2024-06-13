package com.abrullc.spainonrails.details.trainDetailModule

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
import com.abrullc.spainonrails.details.routeDetailModule.RouteDetailFragment
import com.abrullc.spainonrails.mainModule.routesModule.adapters.RoutesListAdapter
import com.abrullc.spainonrails.retrofit.entities.Tren
import com.abrullc.spainonrails.retrofit.services.RutaService
import com.abrullc.spainonrails.retrofit.services.TrenService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrainDetailFragment : Fragment(), OnClickListener {
    private var idTren: Int = 0
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

            val resultTren = trenService.getTren(idTren)

            withContext(Dispatchers.Main) {
                setupTrenInfo(resultTren)
            }
        }, this, requireContext())
    }

    private fun setupTrenInfo(tren: Tren) {
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

        getRutas()
    }

    private fun getRutas() {
        commonFunctions.launchLifeCycleScope({
            val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)

            val resultRutas = rutaService.getRutas().body()!!

            withContext(Dispatchers.Main) {
                mRouteAdapter.submitList(resultRutas)

                /*val totalHeight = measureRecyclerViewHeight(mBinding.rcvRoutesTrain)
                mBinding.rcvRoutesTrain.layoutParams.height = totalHeight
                mBinding.rcvRoutesTrain.requestLayout()*/
            }
        }, this, requireContext())
    }

    private fun measureRecyclerViewHeight(recyclerView: RecyclerView): Int {
        val adapter = recyclerView.adapter ?: return 0
        var totalHeight = 0

        for (i in 0 until adapter.itemCount) {
            val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            adapter.bindViewHolder(viewHolder, i)
            viewHolder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += viewHolder.itemView.measuredHeight
        }

        return totalHeight
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