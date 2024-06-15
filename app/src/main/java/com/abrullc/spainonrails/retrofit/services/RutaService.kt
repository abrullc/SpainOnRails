package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.entities.Ruta
import com.abrullc.spainonrails.retrofit.entities.Tren
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RutaService {
    @GET(Constants.RUTAS_PATH)
    suspend fun getRutas(): Response<MutableList<Ruta>>

    @GET(Constants.RUTA_PATH)
    suspend fun getRuta(@Path("id") id: Int): Response<Ruta>

    @GET(Constants.ESTACIONES_RUTA_PATH)
    suspend fun getEstacionesRuta(@Path("id") id: Int): Response<MutableList<Estacion>>

    @GET(Constants.TREN_RUTA_PATH)
    suspend fun getTrenRuta(@Path("id") id: Int): Response<Tren>
}