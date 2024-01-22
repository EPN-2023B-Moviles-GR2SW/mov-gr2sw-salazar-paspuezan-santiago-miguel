package com.example.examenib

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

class lista_tarea : AppCompatActivity() {
    private val arreglo = BBaseDatosMemoria.arregloProyecto
    private var posicionInit = 0
    private var posicionItemSeleccionado = 0
    private var listaTareas: MutableList<BTarea> = mutableListOf()
    private lateinit var adaptador: ArrayAdapter<BTarea>

    private val desplegarContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    adaptador.notifyDataSetChanged()
                }
            }
        }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_tarea)

        posicionInit = intent.getIntExtra("posicion", -1)
        val txtProyecto = findViewById<TextView>(R.id.txt_nombre_proyecto)
        txtProyecto.text = arreglo[posicionInit].nombreProyecto

        listaTareas = arreglo[posicionInit].ListaTareas
        val listView = findViewById<ListView>(R.id.lv_listTarea)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaTareas
        )
        listView.adapter = adaptador

        val botonAgregar = findViewById<Button>(R.id.btn_agregarTarea)
        botonAgregar.setOnClickListener {
            posicionItemSeleccionado = -1
            irActividadConParametros(add_tarea::class.java)
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
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_tarea -> {
                irActividadConParametros(add_tarea::class.java)
                true
            }
            R.id.mi_eliminar_tarea -> {
                listaTareas.removeAt(posicionItemSeleccionado)
                adaptador.notifyDataSetChanged()
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
