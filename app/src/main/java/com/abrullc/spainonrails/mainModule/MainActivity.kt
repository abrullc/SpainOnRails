package com.abrullc.spainonrails.mainModule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.databinding.ActivityMainBinding
import com.abrullc.spainonrails.loginModule.LoginActivity
import com.abrullc.spainonrails.mainModule.homeModule.HomeFragment
import com.abrullc.spainonrails.mainModule.routesModule.RoutesFragment
import com.abrullc.spainonrails.mainModule.stationsModule.StationsFragment
import com.abrullc.spainonrails.mainModule.trainsModule.TrainsFragment
import com.abrullc.spainonrails.userModule.UserActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

    override fun onResume() {
        super.onResume()

        if (SpainOnRailsApplication.isLogoutRequired) { finish() }

        Glide.with(this@MainActivity)
            .load(SpainOnRailsApplication.usuario.imagen)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .circleCrop()
            .into(mBinding.imgProfile)
    }

    private fun initListeners() {
        with(mBinding) {
            imgNavigationDrawer.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            imgProfile.setOnClickListener { goToUserProfile() }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_profile -> {
                    goToUserProfile()
                }

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

                R.id.drawer_logout -> {
                    commonFunctions.logout(this@MainActivity) {
                        finish()
                    }
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

    private fun goToUserProfile() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
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
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)

            if (currentFragment is TrainsFragment || currentFragment is RoutesFragment || currentFragment is StationsFragment) {
                replaceFragment(HomeFragment(), getString(R.string.home))
                bottomNavigationView.menu.findItem(R.id.bottom_home).isChecked = true
            } else if (currentFragment is HomeFragment) {
                commonFunctions.logout(this@MainActivity) {
                    finish()
                }
            } else {
                super.onBackPressed()
            }
        }
    }
}