package com.example.firebase

import BProyecto
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class lista_tarea : AppCompatActivity() {
    private var proyectoId: String? = null
    private lateinit var adaptador: ArrayAdapter<BTarea>
    private lateinit var listaTareas: MutableList<BTarea>
    private val db = FirebaseFirestore.getInstance()

    private fun updateNombreProyecto(proyectoId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("proyectos").document(proyectoId).get()
            .addOnSuccessListener { documento ->
                val proyecto = documento.toObject(BProyecto::class.java)
                val nombreProyecto = proyecto?.nombreProyecto
                if (nombreProyecto != null) {
                    val txtProyecto = findViewById<TextView>(R.id.txt_nombre_proyecto)
                    txtProyecto.text = nombreProyecto
                }
            }
            .addOnFailureListener { e ->
                // Manejar el error, por ejemplo, mostrando un mensaje al usuario.
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_tarea)

        proyectoId = intent.getStringExtra("proyectoID")
        if (proyectoId != null) {
            updateNombreProyecto(proyectoId!!)
        }
        //listaTareas = arreglo[posicionInit].ListaTareas
        listaTareas = mutableListOf()
        val listView = findViewById<ListView>(R.id.lv_listTarea)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaTareas
        )
        listView.adapter = adaptador

        proyectoId?.let {
            db.collection("proyectos").document(it).collection("ListaTareas")
                .addSnapshotListener{
                    value, e ->
                    if (e != null){
                        return@addSnapshotListener
                    }
                    listaTareas.clear()
                    for (document in value!!) {
                        val tarea = document.toObject<BTarea>()?.apply { id = document.id }
                        tarea?.nombreTarea?.let {
                            nombre ->
                            listaTareas.add(tarea)
                        }
                    }
                    adaptador.notifyDataSetChanged()
                }
        }

        val botonAgregar = findViewById<Button>(R.id.btn_agregarTarea)
        botonAgregar.setOnClickListener {
            val intent = Intent(this, add_tarea::class.java)
            intent.putExtra("proyectoID", proyectoId)
            startActivity(intent)
        }
        registerForContextMenu(listView)
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_tarea, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val tareaSeleccionada = adaptador.getItem(info.position) as BTarea
        return when (item.itemId) {
            R.id.mi_editar_tarea -> {
                val intent = Intent(this, add_tarea::class.java)
                intent.putExtra("proyectoID", proyectoId)
                intent.putExtra("tareaID", tareaSeleccionada.id)
                startActivity(intent)
                true
            }
            R.id.mi_eliminar_tarea -> {
                db.collection("proyectos").document(proyectoId!!)
                    .collection("ListaTareas").document(tareaSeleccionada.id).delete()
                    .addOnSuccessListener {
                        listaTareas.removeAt(info.position) // Elimina la tarea de la lista
                        adaptador.notifyDataSetChanged() // Notifica al adaptador del cambio
                        mostrarSnackbar("Tarea eliminada")
                    }
                    .addOnFailureListener {
                        mostrarSnackbar("Error al eliminar Tarea")
                    }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(findViewById(R.id.lv_listProyecto),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }
}
