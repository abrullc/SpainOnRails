package com.abrullc.spainonrails

import android.app.Application
import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Usuario
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpainOnRailsApplication: Application() {
    companion object {
        private val gson = GsonBuilder()
            .serializeNulls()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Logged user
        lateinit var usuario: Usuario
        var isUsuarioUpdated = false
        var isLogoutRequired = false
    }
}