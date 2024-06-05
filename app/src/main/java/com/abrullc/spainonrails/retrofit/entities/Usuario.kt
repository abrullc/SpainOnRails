package com.abrullc.spainonrails.retrofit.entities

import java.util.Date

data class Usuario(
    val id: Int,
    val username: String,
    val password: String,
    val email: String?
)