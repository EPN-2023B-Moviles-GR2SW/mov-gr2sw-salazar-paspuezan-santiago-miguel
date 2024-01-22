package com.example.examenib

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
    val arreglo = BBaseDatosMemoria.arregloProyecto
    lateinit var adaptador: ArrayAdapter<BProyecto>
    var posicionItem=0

    val getContenido =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if(result.data != null){
                    val datos = result.data
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
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        val botonAgregar = findViewById<Button>(R.id.btn_agregar_proyecto)
        botonAgregar.setOnClickListener {
            posicionItem = -1
            irActividadConParametros(add_proyecto::class.java)
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
        return when (item.itemId) {
            // Editar
            R.id.mi_editar -> {
                irActividadConParametros(add_proyecto::class.java)
                return true
            }
            // Eliminar
            R.id.mi_eliminar -> {
                arreglo.removeAt(posicionItem)
                adaptador.notifyDataSetChanged()
                mostrarSnackbar("Proyecto eliminado")
                return true
            }
            R.id.mi_ver -> {
                irActividadConParametros(lista_tarea::class.java)
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

    fun irActividadConParametros(
        clase: Class<*>
    ) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItem)
        getContenido.launch(intentExplicito)
    }
}