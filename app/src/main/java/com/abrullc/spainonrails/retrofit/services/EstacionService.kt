package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Estacion
import retrofit2.Response
import retrofit2.http.GET

interface EstacionService {
    @GET(Constants.ESTACIONES_PATH)
    suspend fun getEstaciones(): Response<MutableList<Estacion>>
}