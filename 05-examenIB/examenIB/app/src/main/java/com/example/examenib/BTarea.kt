package com.example.examenib

import java.util.Date

class BTarea (
    var id: Int,
    var nombreTarea: String,
    var fechaCreacion: Date = Date()
) {
    override fun toString(): String {
        val fechaFormateada = fechaCreacion.toString()
        return "[$id]\t Tarea: $nombreTarea \n Fecha Creación: $fechaFormateada"
    }
}