package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Estacion
import com.abrullc.spainonrails.retrofit.entities.Ruta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EstacionService {
    @GET(Constants.ESTACIONES_PATH)
    suspend fun getEstaciones(): Response<MutableList<Estacion>>

    @GET(Constants.ESTACION_PATH)
    suspend fun getEstacion(@Path("id") id: Int): Response<Estacion>

    @GET(Constants.RUTAS_ESTACION_PATH)
    suspend fun getRutasEstacion(@Path("id") id: Int): Response<MutableList<Ruta>>
}