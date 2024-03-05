package com.example.firebase

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class BTarea(
    var id: String = "",
    var nombreTarea: String = "",
    var fechaCreacion: Timestamp = Timestamp.now() // Usar Timestamp para compatibilidad con Firestore
) {
    companion object {
        // Formato para mostrar la fecha, si es necesario
        private val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    }
    fun getFechaCreacionFormateada(): String {
        val fecha = fechaCreacion.toDate() // Convertir Timestamp a Date
        return formatoFecha.format(fecha)
    }
    override fun toString(): String {
        return nombreTarea
    }
}
