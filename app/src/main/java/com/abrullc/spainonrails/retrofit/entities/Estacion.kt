package com.abrullc.spainonrails.retrofit.entities

data class Estacion(
    val id: Int,
    var nombre: String,
    var poblacion: String,
    var direccion: String,
    var latitud: Float,
    var longitud: Float,
    var imagen: String?,
    var puntosInteres: MutableList<PuntoInteres>
)
