package com.abrullc.spainonrails.mainModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.databinding.ActivityMainBinding
import com.abrullc.spainonrails.homeModule.HomeFragment
import com.abrullc.spainonrails.routesModule.RoutesFragment
import com.abrullc.spainonrails.stationsModule.StationsFragment
import com.abrullc.spainonrails.trainsModule.TrainsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bottonNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initComponents()
        initListeners()
        replaceFragment(HomeFragment())
    }

    private fun initListeners() {
        bottonNavigationView.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_trains -> {
                    replaceFragment(TrainsFragment())
                    true
                }
                R.id.bottom_routes -> {
                    replaceFragment(RoutesFragment())
                    true
                }
                R.id.bottom_stations -> {
                    replaceFragment(StationsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    private fun initComponents() {
        bottonNavigationView = findViewById(R.id.bottomNav)
    }
}