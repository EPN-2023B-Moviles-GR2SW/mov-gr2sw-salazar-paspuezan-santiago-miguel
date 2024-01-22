package com.example.examenib

class BProyecto(
    val id: Int,
    var nombreProyecto: String,
    var descripcion: String,
    val ListaTareas: MutableList<BTarea> = mutableListOf()
) {
    override fun toString(): String {
        return "Proyecto: $nombreProyecto \n\tDescripción: $descripcion \n\tTotal de tareas: ${ListaTareas.size}"
    }
}