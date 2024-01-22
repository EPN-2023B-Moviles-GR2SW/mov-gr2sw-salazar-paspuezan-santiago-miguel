package com.example.examenib

import java.util.Date

class BBaseDatosMemoria {
    companion object {
        val arregloBTarea1: MutableList<BTarea> = mutableListOf()
        val arregloBTarea2: MutableList<BTarea> = mutableListOf()
        val arregloProyecto: MutableList<BProyecto> = mutableListOf()
        private var contadorIdTarea:Int = 1
        private var contadorIdProyecto:Int = 1
        init {
            arregloBTarea1.add(BTarea(obtenerNuevoIdTarea(), "Hacer los deberes", Date()))
            arregloBTarea1.add(BTarea(obtenerNuevoIdTarea(), "Preparar la exposición", Date()))
            arregloBTarea2.add(BTarea(obtenerNuevoIdTarea(), "Hacer los examens", Date()))
            arregloBTarea2.add(BTarea(obtenerNuevoIdTarea(), "Preparar la comida", Date()))

            arregloProyecto.add(BProyecto(obtenerNuevoIdProyecto(), "Proyecto Casa", "Proyecto de la casa",
                arregloBTarea2))
            arregloProyecto.add(BProyecto(obtenerNuevoIdProyecto(), "Proyecto Escuela", "Proyecto de la escuela", arregloBTarea1))
        }
        fun obtenerNuevoIdTarea(): Int {
            val nuevoId = contadorIdTarea
            contadorIdTarea++
            return nuevoId
        }
        fun obtenerNuevoIdProyecto(): Int {
            val nuevoId = contadorIdProyecto
            contadorIdProyecto++
            return nuevoId
        }
    }
}