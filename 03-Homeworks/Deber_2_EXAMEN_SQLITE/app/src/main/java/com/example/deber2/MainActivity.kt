package com.example.deber2

import BProyecto
import DatabaseHelper
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

class MainActivity : AppCompatActivity() {
    lateinit var adaptador: ArrayAdapter<BProyecto>
    var posicionItem = 0
    lateinit var databaseHelper: DatabaseHelper

    val getContenido =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarProyectosDesdeDB() // Actualizar lista después de agregar/editar
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        val listView = findViewById<ListView>(R.id.lv_listProyecto)

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<BProyecto>())
        listView.adapter = adaptador
        cargarProyectosDesdeDB()

        val botonAgregar = findViewById<Button>(R.id.btn_agregar_proyecto)
        botonAgregar.setOnClickListener {
            posicionItem = -1
            irActividadConParametros(add_proyecto::class.java)
        }
        registerForContextMenu(listView)
    }

    fun cargarProyectosDesdeDB() {
        val proyectos = databaseHelper.getAllProyectos() // Implementar este método en DatabaseHelper
        adaptador.clear()
        adaptador.addAll(proyectos)
        adaptador.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_proyecto, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItem = info.position
    }

    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                irActividadConParametros(add_proyecto::class.java)
                true
            }
            R.id.mi_eliminar -> {
                val proyectoId = adaptador.getItem(posicionItem)?.id ?: return true
                databaseHelper.eliminarProyectoPorId(proyectoId)
                cargarProyectosDesdeDB() // Refrescar la lista después de eliminar
                mostrarSnackbar("Proyecto eliminado")
                true
            }
            R.id.mi_ver -> {
                irActividadConParametros(lista_tarea::class.java)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.lv_listProyecto), texto, Snackbar.LENGTH_LONG).show()
    }

    fun irActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItem)
        getContenido.launch(intentExplicito)
    }
}
