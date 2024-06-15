package com.abrullc.spainonrails.common.utils

object Constants {
    // [RUTAS]
    const val BASE_URL = "http://spainonrails.navelsystems.es/"

    // -- USUARIOS
    const val USUARIOS_PATH = "/usuarios"
    const val USUARIO_PATH = "/usuario/{id}"
    const val VALIDATE_USUARIO_PATH = "/usuario/validate"
    const val VALIDATE_NEW_USUARIO_PATH = "/usuario/{username}/validate"

    // -- ESTACIONES
    const val ESTACIONES_PATH = "/estaciones"
    const val ESTACION_PATH = "/estacion/{id}"
    const val RUTAS_ESTACION_PATH = "/estacion/{id}/rutas"

    // -- PASAJES
    const val PASAJES_PATH = "/pasajes"
    const val PASAJES_USUARIO_PATH = "/pasajes/usuario/{id}"

    // -- PLANES DE VIAJE
    const val PLANES_VIAJE_PATH = "/planesViaje"
    const val VISITAS_PLAN_VIAJE = "/planViaje/{id}/puntosInteres"
    const val PLANES_VIAJE_USUARIO_PATH = "planesViaje/usuario/{id}"

    // -- PUNTOS DE INTERÉS
    const val PUNTOS_INTERES_PATH = "/puntosInteres"
    const val PUNTOS_INTERES_ESTACION_PATH = "/puntosInteres/estacion/{id}"

    // -- RUTAS
    const val RUTAS_PATH = "/rutas"
    const val RUTA_PATH = "/ruta/{id}"
    const val ESTACIONES_RUTA_PATH = "/ruta/{id}/estaciones"

    // -- TRENES
    const val TRENES_PATH = "/trenes"
    const val TREN_PATH = "/tren/{id}"
    const val RUTAS_TREN_PATH = "tren/{id}/rutas"
}