package com.example.firebase

import BProyecto
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {
    //val arreglo = BBaseDatosMemoria.arregloProyecto
    lateinit var adaptador: ArrayAdapter<BProyecto>
    var posicionItem=0
    var proyectos = mutableListOf<BProyecto>()
    private val db = FirebaseFirestore.getInstance()

    val getContenido =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(result.data != null){
                    //val datos = result.data
                    adaptador.notifyDataSetChanged()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.lv_listProyecto)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            this.proyectos
        )
        listView.adapter = adaptador
        db.collection("proyectos").addSnapshotListener{
            snapshot, e ->
            if (e != null){
                return@addSnapshotListener
            }
            if (snapshot != null) {
                proyectos.clear()
                for (documento in snapshot.documents) {
                    val proyecto = documento.toObject<BProyecto>()?.apply { id = documento.id }
                    proyecto?.nombreProyecto?.let {
                        nombre ->
                        proyectos.add(proyecto)
                    }
                }
                adaptador.notifyDataSetChanged()
            }
        }


        val botonAgregar = findViewById<Button>(R.id.btn_agregar_proyecto)
        botonAgregar.setOnClickListener {
            val intent = Intent(this, add_proyecto::class.java)

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
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_proyecto, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItem = id
    }
    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val proyectoSeleccionado = adaptador.getItem(info.position) as BProyecto
        return when (item.itemId) {
            // Editar
            R.id.mi_editar -> {
                val intent = Intent(this, add_proyecto::class.java)
                intent.putExtra("proyectoID", proyectoSeleccionado.id)
                startActivity(intent)
                return true
            }
            // Eliminar
            R.id.mi_eliminar -> {
                //arreglo.removeAt(posicionItem)
                val db = FirebaseFirestore.getInstance()
                db.collection("proyectos").document(proyectoSeleccionado.id).delete()
                    .addOnSuccessListener { mostrarSnackbar("Proyecto eliminado") }
                    .addOnFailureListener { mostrarSnackbar("Error al eliminar proyecto") }
                adaptador.notifyDataSetChanged()
                mostrarSnackbar("Proyecto eliminado")
                return true
            }
            R.id.mi_ver -> {
                val intent = Intent(this, lista_tarea::class.java)
                intent.putExtra("proyectoID", proyectoSeleccionado.id)
                startActivity(intent)
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