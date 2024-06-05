package com.abrullc.spainonrails

import android.app.Application
import com.abrullc.spainonrails.retrofit.entities.Usuario

class SpainOnRailsApplication: Application() {
    companion object {
        lateinit var usuario: Usuario
    }
}