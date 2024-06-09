package com.abrullc.spainonrails.retrofit.entities

data class Estacion(
    val id: Int,
    var nombre: String,
    var poblacion: String,
    var direccion: String,
    var longitud: Float,
    var latitud: Float,
    var imagen: String?,
    var puntosInteres: MutableList<PuntoInteres>
)
