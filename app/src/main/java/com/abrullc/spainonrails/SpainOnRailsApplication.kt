package com.abrullc.spainonrails

import android.app.Application
import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.entities.Pasaje
import com.abrullc.spainonrails.retrofit.entities.PlanViaje
import com.abrullc.spainonrails.retrofit.entities.PuntoInteres
import com.abrullc.spainonrails.retrofit.entities.Ruta
import com.abrullc.spainonrails.retrofit.entities.Tren
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

        // General application data
        lateinit var trenes: MutableList<Tren>
        lateinit var rutas: MutableList<Ruta>
        lateinit var estaciones: MutableList<Estacion>
        lateinit var puntosInteres: MutableList<PuntoInteres>
        lateinit var pasajes: MutableList<Pasaje>
        lateinit var planesViaje: MutableList<PlanViaje>

        // User specific data
        lateinit var pasajesUsuario: MutableList<Pasaje>
        lateinit var planesViajeUsuario: MutableList<PlanViaje>
    }
}