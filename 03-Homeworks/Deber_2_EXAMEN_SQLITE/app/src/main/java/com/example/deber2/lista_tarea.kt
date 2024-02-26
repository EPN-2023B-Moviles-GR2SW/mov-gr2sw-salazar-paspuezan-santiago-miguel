package com.example.deber2

import BTarea
import DatabaseHelper
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
import java.util.*


class lista_tarea : AppCompatActivity() {
    private var posicionInit = 0
    private var posicionItemSeleccionado = 0
    private lateinit var adaptador: ArrayAdapter<BTarea>
    private lateinit var databaseHelper: DatabaseHelper

    private val desplegarContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarTareasDesdeDB() // Refrescar lista después de añadir/editar
            }
        }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_tarea)

        databaseHelper = DatabaseHelper(this)

        posicionInit = intent.getIntExtra("posicion", -1)
        val txtProyecto = findViewById<TextView>(R.id.txt_nombre_proyecto)
        // Aquí deberías recuperar el nombre del proyecto usando posicionInit desde SQLite
        txtProyecto.text = "Nombre del Proyecto" // Actualizar con valor real de SQLite

        val listView = findViewById<ListView>(R.id.lv_listTarea)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<BTarea>()
        )
        listView.adapter = adaptador

        cargarTareasDesdeDB()

        val botonAgregar = findViewById<Button>(R.id.btn_agregarTarea)
        botonAgregar.setOnClickListener {
            posicionItemSeleccionado = -1
            irActividadConParametros(add_tarea::class.java)
        }
        registerForContextMenu(listView)
    }

    fun cargarTareasDesdeDB() {
        val tareas = databaseHelper.getTareasPorProyectoId(posicionInit) // Implementar este método
        adaptador.clear()
        adaptador.addAll(tareas)
        adaptador.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_tarea, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val posicionTarea = info.position
        return when (item.itemId) {
            R.id.mi_editar_tarea -> {
                irActividadConParametros(add_tarea::class.java)
                true
            }
            R.id.mi_eliminar_tarea -> {
                val tareaId = adaptador.getItem(posicionTarea)?.id // Suponiendo que tu adaptador maneja objetos BTarea que tienen un 'id'.
                if (tareaId != null) {
                    databaseHelper.eliminarTareaPorId(tareaId)
                    cargarTareasDesdeDB() // Refrescar lista después de eliminar
                } else {
                    // Manejar el caso de que tareaId sea null.
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun irActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionInit)
        intentExplicito.putExtra("posicionItemSeleccionado", posicionItemSeleccionado)
        desplegarContenido.launch(intentExplicito)
    }
}
