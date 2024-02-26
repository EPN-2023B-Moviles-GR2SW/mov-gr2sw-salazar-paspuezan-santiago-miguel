package com.example.deber2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import java.util.Date

class add_tarea : AppCompatActivity() {
    val arreglo = BBaseDatosMemoria.arregloProyecto
    var listaTareas: MutableList<BTarea> = mutableListOf()
    var posicionInit = -1
    var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        posicionInit = intent.getIntExtra("posicion", -1)
        posicionItemSeleccionado = intent.getIntExtra("posicionItemSeleccionado", -1)
        listaTareas = arreglo[posicionInit].ListaTareas

        val botonCrearTarea = findViewById<Button>(R.id.btn_agregar_nueva_tarea)
        val nombreDeTarea = findViewById<EditText>(R.id.input_nueva_tarea)

        if (posicionItemSeleccionado != -1) {
            // Para editar tarea existente
            nombreDeTarea.setText(listaTareas[posicionItemSeleccionado].nombreTarea)
        }

        botonCrearTarea.setOnClickListener {
            val nombre = nombreDeTarea.text.toString()

            if (posicionItemSeleccionado == -1) {
                // Agregar nueva tarea
                listaTareas.add(
                    BTarea(
                        BBaseDatosMemoria.obtenerNuevoIdTarea(),
                        nombre,
                        Date()
                    )
                )
            } else {
                // Editar tarea existente
                listaTareas[posicionItemSeleccionado].nombreTarea = nombre

            }
            responseOK()
        }
    }

    private fun responseOK() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("posicion", posicionItemSeleccionado)
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }
}
