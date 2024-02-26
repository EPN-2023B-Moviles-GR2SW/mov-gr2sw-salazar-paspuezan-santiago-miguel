package com.example.deber2

import DatabaseHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class add_proyecto : AppCompatActivity() {
    var posicionSeleccionada = -1
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proyecto)
        databaseHelper = DatabaseHelper(this)

        posicionSeleccionada = intent.getIntExtra("posicion", -1)
        val botonCrearProyecto = findViewById<Button>(R.id.btn_agregar_pry)
        val inputNombreProyecto = findViewById<EditText>(R.id.input_nombre_nuevo_pry)
        val inputDescripcionProyecto = findViewById<EditText>(R.id.input_descripcion_nuevo_pry)
        if (posicionSeleccionada != -1) {
            inputNombreProyecto.setText("Nombre del Proyecto") // Reemplaza con valor de SQLite
            inputDescripcionProyecto.setText("Descripción del Proyecto") // Reemplaza con valor de SQLite
        }

        botonCrearProyecto.setOnClickListener {

            if (posicionSeleccionada == -1) {
                databaseHelper.addProyecto(inputNombreProyecto.text.toString(), inputDescripcionProyecto.text.toString())
            } else {
                databaseHelper.updateProyecto(posicionSeleccionada,inputNombreProyecto.text.toString(), inputDescripcionProyecto.text.toString())
            }
            responseOK()
        }
    }

    private fun responseOK() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("posicion", posicionSeleccionada)
        setResult(RESULT_OK, intentDevolverParametros)
        finish()
    }
}