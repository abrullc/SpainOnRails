package com.abrullc.spainonrails.retrofit.entities

data class PlanViaje(
    val id: Int,
    var nombre: String,
    var visitas: MutableList<Visita>
)
