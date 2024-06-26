package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Ruta
import com.abrullc.spainonrails.retrofit.entities.Tren
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrenService {
    @GET(Constants.TRENES_PATH)
    suspend fun getTrenes(): Response<MutableList<Tren>>

    @GET(Constants.TREN_PATH)
    suspend fun getTren(@Path("id") id: Int): Response<Tren>

    @GET(Constants.RUTAS_TREN_PATH)
    suspend fun getRutasTren(@Path("id") id: Int): Response<MutableList<Ruta>>
}