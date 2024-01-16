package com.example.b2023sw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class ECrudEntrenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_entrenador)
        val botonBuscarBDD = findViewById<Button>(R.id.btn_buscar_bdd)
        botonBuscarBDD.setOnClickListener {
                val id = findViewById<EditText>(R.id.input_id)
                val nombre = findViewById<EditText>(R.id.input_name)
                val descripcion = findViewById<EditText>(R.id.input_description)
                val entrenador = EBaseDeDatos.tablaEntrenador!!
                    .consultarEntrenadorPorID(
                        id.text.toString().toInt()
                    )
            id.setText(entrenador.id.toString())
            nombre.setText(entrenador.nombre)
            descripcion.setText(entrenador.description)

            if(entrenador.id ==0 ){
                mostrarSnackbar("Usu. No encontrado")
            }
            id.setText(entrenador.id.toString())
            nombre.setText(entrenador.nombre)
            descripcion.setText(entrenador.description)
            mostrarSnackbar("Busqueda exitosa")
        }
        val botonCrearBDD = findViewById<Button>(R.id.btn_crear_bdd)
        botonCrearBDD
            .setOnClickListener{
                val nombre = findViewById<EditText>(R.id.input_name)
                val descripcion = findViewById<EditText>(R.id.input_description)
                val respuesta = EBaseDeDatos
                    .tablaEntrenador!!.crearEntrenador(
                        nombre.text.toString(),
                        descripcion.text.toString()
                    )
                if(respuesta) mostrarSnackbar("Ent. Creado")
            }
        val botonActualizarBDD = findViewById<Button>(R.id.btn_actualizar_bdd)
        botonActualizarBDD
            .setOnClickListener {
                val id = findViewById<EditText>(R.id.input_id)
                val nombre = findViewById<EditText>(R.id.input_name)
                val descripcion = findViewById<EditText>(R.id.input_description)
                val respuesta = EBaseDeDatos
                    .tablaEntrenador!!.actualizarEntrenadorFormulario(
                        nombre.text.toString(),
                        descripcion.text.toString(),
                        id.text.toString().toInt()
                    )
                if(respuesta) mostrarSnackbar("Ent. Actualizado")
            }
        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_bdd)
        botonEliminarBDD
            .setOnClickListener {
                val id = findViewById<EditText>(R.id.input_id)
                val respuesta = EBaseDeDatos
                    .tablaEntrenador!!.eliminarEntradorFormulario(
                        id.text.toString().toInt(
                    )
                    )
                if(respuesta) mostrarSnackbar("Ent. Eliminado")
            }
    }
         fun mostrarSnackbar(texto: String){
        Snackbar
            .make(
            findViewById(R.id.cl_sqlite),
            texto,
            Snackbar.LENGTH_LONG
        )
            .show()
    }
}
