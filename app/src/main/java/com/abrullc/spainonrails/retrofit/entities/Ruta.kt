package com.abrullc.spainonrails.retrofit.entities

data class Ruta(
    val id: Int,
    var origen: String,
    var destino: String,
    var descripcion: String,
    var tren: Tren
)
