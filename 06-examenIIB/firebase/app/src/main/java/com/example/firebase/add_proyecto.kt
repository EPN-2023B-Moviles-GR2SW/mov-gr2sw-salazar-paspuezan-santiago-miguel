package com.example.firebase

import BProyecto
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.common.escape.Escapers
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore

class add_proyecto : AppCompatActivity() {
    private var proyectoID: String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proyecto)

        val botonCrearProyecto = findViewById<android.widget.Button>(R.id.btn_agregar_pry)
        val inputNombreProyecto = findViewById<android.widget.EditText>(R.id.input_nombre_nuevo_pry)
        val inputDescripcionProyecto =
            findViewById<android.widget.EditText>(R.id.input_descripcion_nuevo_pry)

        proyectoID = intent.getStringExtra("proyectoID")

        proyectoID?.let {
            // Cargar datos del proyecto para editar
            db.collection("proyectos").document(it).get()
                .addOnSuccessListener { documento ->
                    val proyecto = documento.toObject(BProyecto::class.java)
                    inputNombreProyecto.setText(proyecto?.nombreProyecto)
                    inputDescripcionProyecto.setText(proyecto?.descripcion)
                }
        }
        botonCrearProyecto.setOnClickListener {
            val nombreProyecto = inputNombreProyecto.text.toString()
            val descripcion = inputDescripcionProyecto.text.toString()
            if (proyectoID.isNullOrEmpty()) {
                val Nuevoproyecto = hashMapOf(
                    "nombreProyecto" to nombreProyecto,
                    "descripcion" to descripcion
                )
                db.collection("proyectos").add(Nuevoproyecto)
                    .addOnSuccessListener { documentReference ->
                        responseOK()
                    }
            } else {
                // editando un proyecto
                val nuevoUpdated = mapOf(
                    "nombreProyecto" to nombreProyecto,
                    "descripcion" to descripcion
                )
                db.collection("proyectos").document(proyectoID!!).update(nuevoUpdated)
                    .addOnSuccessListener {
                        responseOK()
                    }
            }
        }
    }

    private fun responseOK() {
        setResult(
            RESULT_OK
        )
        finish()
    }
}
