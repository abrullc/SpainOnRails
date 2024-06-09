package com.abrullc.spainonrails.retrofit.entities

data class Usuario(
    val id: Int,
    var username: String,
    var password: String,
    var email: String?,
    var imagen: String?
)