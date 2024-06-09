package com.abrullc.spainonrails.retrofit.entities

import java.sql.Date

data class Pasaje(
    val id: Int,
    var salida: Date,
    var llegada: Date,
    var precio: Float,
    var habitacion: String,
    var ruta: Ruta,
    var usuario: Usuario
)
