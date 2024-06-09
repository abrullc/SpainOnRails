package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.Pasaje
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PasajeService {
    @GET(Constants.PASAJES_PATH)
    suspend fun getPasajes(): Response<MutableList<Pasaje>>

    @GET(Constants.PASAJES_USUARIO_PATH)
    suspend fun getPasajesUsuario(@Path("id") id: Int): Response<MutableList<Pasaje>>
}