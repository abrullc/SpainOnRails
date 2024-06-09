package com.abrullc.spainonrails.retrofit.entities

import java.sql.Date

data class Visita(
    var fecha: Date,
    val puntoInteres: PuntoInteres
)
