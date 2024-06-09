package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.PuntoInteres
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PuntoInteresService {
    @GET(Constants.PUNTOS_INTERES_PATH)
    suspend fun getPuntosInteres(): Response<MutableList<PuntoInteres>>

    @GET(Constants.PUNTOS_INTERES_ESTACION_PATH)
    suspend fun getPuntosInteresEstacion(@Path("id") id: Int): Response<MutableList<PuntoInteres>>
}