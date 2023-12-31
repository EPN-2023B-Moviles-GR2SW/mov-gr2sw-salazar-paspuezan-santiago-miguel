package models

import java.util.Date

data class Task(
    var id: Int,
    var nombreTarea: String,
    var descripcionTarea: String,
    var fechaCreacion: Date = Date(),
    var completada: Boolean = false
) {
    constructor(id: Int, nombreTarea: String, descripcionTarea: String, completada: Boolean = false) :
            this(id, nombreTarea, descripcionTarea, Date(), completada)
    fun completeTask() {
        this.completada = true
    }

    override fun toString(): String {
        val estado = if (completada) "Completada" else "Pendiente"
        val fechaFormateada = fechaCreacion.toString()
        return "ID: $id\tTarea: $nombreTarea \tDescripción: $descripcionTarea \tEstado: $estado \tFecha Creación: $fechaFormateada"
    }
}
