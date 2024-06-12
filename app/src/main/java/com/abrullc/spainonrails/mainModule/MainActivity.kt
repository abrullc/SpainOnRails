package com.abrullc.spainonrails.mainModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.ActivityMainBinding
import com.abrullc.spainonrails.mainModule.homeModule.HomeFragment
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.retrofit.services.PasajeService
import com.abrullc.spainonrails.retrofit.services.PlanViajeService
import com.abrullc.spainonrails.retrofit.services.PuntoInteresService
import com.abrullc.spainonrails.retrofit.services.RutaService
import com.abrullc.spainonrails.retrofit.services.TrenService
import com.abrullc.spainonrails.mainModule.routesModule.RoutesFragment
import com.abrullc.spainonrails.mainModule.stationsModule.StationsFragment
import com.abrullc.spainonrails.mainModule.trainsModule.TrainsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var commonFunctions: CommonFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        commonFunctions = CommonFunctions()

        initComponents()
        initListeners()
        replaceFragment(HomeFragment(), getString(R.string.home))
    }

    private fun loadData(onDataLoaded: () -> Unit) {
        val trenService = SpainOnRailsApplication.retrofit.create(TrenService::class.java)
        val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)
        val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)
        val puntoInteresService = SpainOnRailsApplication.retrofit.create(PuntoInteresService::class.java)
        val pasajeService = SpainOnRailsApplication.retrofit.create(PasajeService::class.java)
        val planViajeService = SpainOnRailsApplication.retrofit.create(PlanViajeService::class.java)

        // General application data
        commonFunctions.launchLifeCycleScope({
            val resultTrenes = trenService.getTrenes().body()!!
            SpainOnRailsApplication.trenes = resultTrenes
        },this, this)

        commonFunctions.launchLifeCycleScope({
            val resultRutas = rutaService.getRutas()

            SpainOnRailsApplication.rutas = resultRutas.body()!!
            for (ruta in SpainOnRailsApplication.rutas) {
                val resultEstacionesRuta = rutaService.getEstacionesRuta(ruta.id)
                if (resultEstacionesRuta.body()!!.isNotEmpty()) {
                    ruta.estaciones = resultEstacionesRuta.body()!!
                }
            }
        }, this, this)

        commonFunctions.launchLifeCycleScope({
            val resultEstaciones = estacionService.getEstaciones()

            SpainOnRailsApplication.estaciones = resultEstaciones.body()!!
            for (estacion in SpainOnRailsApplication.estaciones) {
                val resultPuntosInteresEstacion = puntoInteresService.getPuntosInteresEstacion(estacion.id)
                if (resultPuntosInteresEstacion.body()!!.isNotEmpty()) {
                    estacion.puntosInteres = resultPuntosInteresEstacion.body()!!
                }
            }
        }, this, this)

        commonFunctions.launchLifeCycleScope({
            val resultPuntoInteres = puntoInteresService.getPuntosInteres()

            SpainOnRailsApplication.puntosInteres = resultPuntoInteres.body()!!
        }, this, this)

        commonFunctions.launchLifeCycleScope({
            val resultPasajes = pasajeService.getPasajes()

            SpainOnRailsApplication.pasajes = resultPasajes.body()!!
        }, this, this)

        commonFunctions.launchLifeCycleScope({
            val resultPlanesViaje = planViajeService.getPlanesViaje()

            SpainOnRailsApplication.planesViaje = resultPlanesViaje.body()!!
            for (planViaje in SpainOnRailsApplication.planesViaje) {
                val resultVisitasPlanViaje = planViajeService.getVisitasPlanViaje(planViaje.id)
                if (resultVisitasPlanViaje.body()!!.isNotEmpty()) {
                    planViaje.visitas = resultVisitasPlanViaje.body()!!
                }
            }
        }, this, this)

        // User specific data
        commonFunctions.launchLifeCycleScope({
            val resultPasajesUsuario = pasajeService.getPasajesUsuario(SpainOnRailsApplication.usuario.id)

            if (resultPasajesUsuario.body()!!.isNotEmpty()) {
                SpainOnRailsApplication.pasajesUsuario = resultPasajesUsuario.body()!!
            }
        }, this, this)

        commonFunctions.launchLifeCycleScope({
            val resultPlanesViajeUsuario = planViajeService.getPlanesViajeUsuario(SpainOnRailsApplication.usuario.id)

            if (resultPlanesViajeUsuario.body()!!.isNotEmpty()) {
                SpainOnRailsApplication.planesViajeUsuario = resultPlanesViajeUsuario.body()!!
            }
        }, this, this)

        onDataLoaded()
    }

    private fun initListeners() {
        mBinding.imgNavigationDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_home -> {
                    replaceFragment(HomeFragment(), getString(R.string.home))
                    bottomNavigationView.menu.findItem(R.id.bottom_home).isChecked = true
                }
                R.id.drawer_trains -> {
                    replaceFragment(TrainsFragment(), getString(R.string.trains))
                    bottomNavigationView.menu.findItem(R.id.bottom_trains).isChecked = true
                }
                R.id.drawer_routes -> {
                    replaceFragment(RoutesFragment(), getString(R.string.routes))
                    bottomNavigationView.menu.findItem(R.id.bottom_routes).isChecked = true
                }
                R.id.drawer_stations -> {
                    replaceFragment(StationsFragment(), getString(R.string.stations))
                    bottomNavigationView.menu.findItem(R.id.bottom_stations).isChecked = true
                }
            }
            navigationView.setCheckedItem(menuItem.itemId)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment(), getString(R.string.home))
                    navigationView.setCheckedItem(R.id.drawer_home)
                }
                R.id.bottom_trains -> {
                    replaceFragment(TrainsFragment(), getString(R.string.trains))
                    navigationView.setCheckedItem(R.id.drawer_trains)
                }
                R.id.bottom_routes -> {
                    replaceFragment(RoutesFragment(), getString(R.string.routes))
                    navigationView.setCheckedItem(R.id.drawer_routes)
                }
                R.id.bottom_stations -> {
                    replaceFragment(StationsFragment(), getString(R.string.stations))
                    navigationView.setCheckedItem(R.id.drawer_stations)
                }
            }
            menuItem.isChecked = true
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()

        mBinding.tvScreenTitle.text = title
    }

    private fun initComponents() {
        bottomNavigationView = mBinding.bottomNav
        drawerLayout = mBinding.drawerLayout
        navigationView = mBinding.navDrawer
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}