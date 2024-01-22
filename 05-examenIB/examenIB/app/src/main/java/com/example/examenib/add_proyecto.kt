package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class add_proyecto : AppCompatActivity() {
    val arreglo = BBaseDatosMemoria.arregloProyecto
    var posicionSeleccionada = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proyecto)
        posicionSeleccionada = intent.getIntExtra("posicion", -1)
        val botonCrearProyecto = findViewById<android.widget.Button>(R.id.btn_agregar_pry)

        if (posicionSeleccionada != -1) {
            val inputNombreProyecto = findViewById<android.widget.EditText>(R.id.input_nombre_nuevo_pry)
            val inputDescripcionProyecto = findViewById<android.widget.EditText>(R.id.input_descripcion_nuevo_pry)
            inputNombreProyecto.setText(arreglo[posicionSeleccionada].nombreProyecto)
            inputDescripcionProyecto.setText(arreglo[posicionSeleccionada].descripcion)
        }
        if (posicionSeleccionada == -1){
            botonCrearProyecto.setOnClickListener{
                val inputNombreProyecto = findViewById<android.widget.EditText>(R.id.input_nombre_nuevo_pry)
                val inputDescripcionProyecto = findViewById<android.widget.EditText>(R.id.input_descripcion_nuevo_pry)
                arreglo.add(
                    BProyecto(
                        BBaseDatosMemoria.obtenerNuevoIdProyecto(),
                        inputNombreProyecto.text.toString(),
                        inputDescripcionProyecto.text.toString(),
                        mutableListOf()
                    )
                )
                responseOK()
            }
        }
        if (posicionSeleccionada != -1){
            botonCrearProyecto.setOnClickListener{
                var inputNombreProyecto = findViewById<android.widget.EditText>(R.id.input_nombre_nuevo_pry)
                var inputDescripcionProyecto = findViewById<android.widget.EditText>(R.id.input_descripcion_nuevo_pry)
                arreglo[posicionSeleccionada].nombreProyecto = inputNombreProyecto.text.toString()
                arreglo[posicionSeleccionada].descripcion = inputDescripcionProyecto.text.toString()
                responseOK()
            }
        }


    }

    private fun responseOK() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("posicion", posicionSeleccionada)
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }
}