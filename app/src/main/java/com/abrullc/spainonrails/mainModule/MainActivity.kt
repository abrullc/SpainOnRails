package com.abrullc.spainonrails.mainModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.databinding.ActivityMainBinding
import com.abrullc.spainonrails.homeModule.HomeFragment
import com.abrullc.spainonrails.routesModule.RoutesFragment
import com.abrullc.spainonrails.stationsModule.StationsFragment
import com.abrullc.spainonrails.trainsModule.TrainsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initComponents()
        initListeners()
        replaceFragment(HomeFragment())
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