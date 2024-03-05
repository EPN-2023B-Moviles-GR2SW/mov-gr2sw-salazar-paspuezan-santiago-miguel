package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class add_tarea : AppCompatActivity() {
    private var proyectoId: String? = null
    private var tareaId: String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

       proyectoId = intent.getStringExtra("proyectoID")
        tareaId = intent.getStringExtra("tareaID")


        val botonCrearTarea = findViewById<Button>(R.id.btn_agregar_nueva_tarea)
        val nombreDeTarea = findViewById<EditText>(R.id.input_nueva_tarea)

        if (tareaId.isNullOrEmpty()) {
            tareaId?.let {
                db.collection("proyectos").document(proyectoId!!)
                    .collection("ListaTareas")
                    .document(it).get()
                    .addOnSuccessListener { documento ->
                        if (documento != null) {
                            val tarea = documento.toObject(BTarea::class.java)
                            nombreDeTarea.setText(tarea?.nombreTarea)
                        }
                    }
            }
        }

        botonCrearTarea.setOnClickListener {
            val nombre = nombreDeTarea.text.toString()
            if (tareaId == null) {
                val nuevaTarea = mapOf("nombreTarea" to nombre)
                proyectoId?.let {
                    db.collection("proyectos").document(it)
                        .collection("ListaTareas")
                        .add(nuevaTarea)
                }
            }else{
                val tareaActualizada = mapOf(
                    "nombreTarea" to nombre
                )
                proyectoId?.let {
                    db.collection("proyectos").document(it)
                        .collection("ListaTareas")
                        .document(tareaId!!)
                        .update(tareaActualizada)
                }
                }
            responseOK()
        }
    }

    private fun responseOK() {
        setResult(
            RESULT_OK,
        )
        finish()
    }
}
