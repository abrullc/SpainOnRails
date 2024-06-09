package com.abrullc.spainonrails.mainModule

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.databinding.ActivityMainBinding
import com.abrullc.spainonrails.homeModule.HomeFragment
import com.abrullc.spainonrails.retrofit.services.EstacionService
import com.abrullc.spainonrails.retrofit.services.PasajeService
import com.abrullc.spainonrails.retrofit.services.PlanViajeService
import com.abrullc.spainonrails.retrofit.services.PuntoInteresService
import com.abrullc.spainonrails.retrofit.services.RutaService
import com.abrullc.spainonrails.retrofit.services.TrenService
import com.abrullc.spainonrails.routesModule.RoutesFragment
import com.abrullc.spainonrails.stationsModule.StationsFragment
import com.abrullc.spainonrails.trainsModule.TrainsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loadData()
        initComponents()
        initListeners()
        replaceFragment(HomeFragment())
    }

    private fun loadData() {
        val trenService = SpainOnRailsApplication.retrofit.create(TrenService::class.java)
        val rutaService = SpainOnRailsApplication.retrofit.create(RutaService::class.java)
        val estacionService = SpainOnRailsApplication.retrofit.create(EstacionService::class.java)
        val puntoInteresService = SpainOnRailsApplication.retrofit.create(PuntoInteresService::class.java)
        val pasajeService = SpainOnRailsApplication.retrofit.create(PasajeService::class.java)
        val planViajeService = SpainOnRailsApplication.retrofit.create(PlanViajeService::class.java)

        lifecycleScope.launch {
            try {
                // General application data
                val resultTrenes = trenService.getTrenes()
                val resultRutas = rutaService.getRutas()
                val resultEstaciones = estacionService.getEstaciones()
                val resultPuntoInteres = puntoInteresService.getPuntosInteres()
                val resultPasajes = pasajeService.getPasajes()
                val resultPlanesViaje = planViajeService.getPlanesViaje()

                SpainOnRailsApplication.trenes = resultTrenes.body()!!

                SpainOnRailsApplication.rutas = resultRutas.body()!!
                for (ruta in SpainOnRailsApplication.rutas) {
                    val resultEstacionesRuta = rutaService.getEstacionesRuta(ruta.id)
                    if (resultEstacionesRuta.body()!!.isNotEmpty()) {
                        ruta.estaciones = resultEstacionesRuta.body()!!
                    }
                }

                SpainOnRailsApplication.estaciones = resultEstaciones.body()!!
                for (estacion in SpainOnRailsApplication.estaciones) {
                    val resultPuntosInteresEstacion = puntoInteresService.getPuntosInteresEstacion(estacion.id)
                    if (resultPuntosInteresEstacion.body()!!.isNotEmpty()) {
                        estacion.puntosInteres = resultPuntosInteresEstacion.body()!!
                    }
                }

                SpainOnRailsApplication.puntosInteres = resultPuntoInteres.body()!!

                SpainOnRailsApplication.pasajes = resultPasajes.body()!!

                SpainOnRailsApplication.planesViaje = resultPlanesViaje.body()!!
                for (planViaje in SpainOnRailsApplication.planesViaje) {
                    val resultVisitasPlanViaje = planViajeService.getVisitasPlanViaje(planViaje.id)
                    if (resultVisitasPlanViaje.body()!!.isNotEmpty()) {
                        planViaje.visitas = resultVisitasPlanViaje.body()!!
                    }
                }

                // User specific data
                val resultPasajesUsuario = pasajeService.getPasajesUsuario(SpainOnRailsApplication.usuario.id)
                val resultPlanesViajeUsuario = planViajeService.getPlanesViajeUsuario(SpainOnRailsApplication.usuario.id)

                if (resultPasajesUsuario.body()!!.isNotEmpty()) {
                    SpainOnRailsApplication.pasajesUsuario = resultPasajesUsuario.body()!!
                }

                if (resultPlanesViajeUsuario.body()!!.isNotEmpty()) {
                    SpainOnRailsApplication.planesViajeUsuario = resultPlanesViajeUsuario.body()!!
                }

            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when(it.code()) {
                        400 -> {
                            Toast.makeText(this@MainActivity, R.string.main_error_server, Toast.LENGTH_SHORT).show()
                        }
                        else ->
                            Toast.makeText(this@MainActivity, R.string.unknown_error_server, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        mBinding.imgNavigationDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_home -> {
                    replaceFragment(HomeFragment())
                    bottomNavigationView.menu.findItem(R.id.bottom_home).isChecked = true
                }
                R.id.drawer_trains -> {
                    replaceFragment(TrainsFragment())
                    bottomNavigationView.menu.findItem(R.id.bottom_trains).isChecked = true
                }
                R.id.drawer_routes -> {
                    replaceFragment(RoutesFragment())
                    bottomNavigationView.menu.findItem(R.id.bottom_routes).isChecked = true
                }
                R.id.drawer_stations -> {
                    replaceFragment(StationsFragment())
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
                    replaceFragment(HomeFragment())
                    navigationView.setCheckedItem(R.id.drawer_home)
                }
                R.id.bottom_trains -> {
                    replaceFragment(TrainsFragment())
                    navigationView.setCheckedItem(R.id.drawer_trains)
                }
                R.id.bottom_routes -> {
                    replaceFragment(RoutesFragment())
                    navigationView.setCheckedItem(R.id.drawer_routes)
                }
                R.id.bottom_stations -> {
                    replaceFragment(StationsFragment())
                    navigationView.setCheckedItem(R.id.drawer_stations)
                }
            }
            menuItem.isChecked = true
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
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