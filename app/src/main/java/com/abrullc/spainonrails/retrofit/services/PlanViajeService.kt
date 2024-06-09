package com.abrullc.spainonrails.retrofit.services

import com.abrullc.spainonrails.common.utils.Constants
import com.abrullc.spainonrails.retrofit.entities.PlanViaje
import com.abrullc.spainonrails.retrofit.entities.Visita
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlanViajeService {
    @GET(Constants.PLANES_VIAJE_PATH)
    suspend fun getPlanesViaje(): Response<MutableList<PlanViaje>>

    @GET(Constants.VISITAS_PLAN_VIAJE)
    suspend fun getVisitasPlanViaje(@Path("id") id: Int): Response<MutableList<Visita>>

    @GET(Constants.PLANES_VIAJE_USUARIO_PATH)
    suspend fun getPlanesViajeUsuario(@Path("id") id: Int): Response<MutableList<PlanViaje>>
}