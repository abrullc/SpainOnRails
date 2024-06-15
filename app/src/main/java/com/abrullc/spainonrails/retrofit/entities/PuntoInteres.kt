package com.abrullc.spainonrails.retrofit.entities

data class PuntoInteres(
    val id: Int,
    var nombre: String,
    var direccion: String,
    var descripcion: String,
    var latitud: Float,
    var longitud: Float,
    var imagen: String?,
    val estacion: Estacion
)
