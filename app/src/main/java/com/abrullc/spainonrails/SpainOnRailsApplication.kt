package com.abrullc.spainonrails

import android.app.Application
import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Usuario
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpainOnRailsApplication: Application() {
    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Logged user
        lateinit var usuario: Usuario
    }
}