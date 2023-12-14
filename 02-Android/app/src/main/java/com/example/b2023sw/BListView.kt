package com.example.b2023sw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar

class BListView : AppCompatActivity() {
    val arreglo = BBaseDatosMemoria.arregloBEntrenador
    var posicionItemSeleccionado = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blist_view)
        val listView = findViewById<ListView>(R.id.lv_listView)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter=adaptador
        adaptador.notifyDataSetChanged()

        val botonAnadirListView = findViewById<Button>(R.id.btn_aniadir_list_view)
        botonAnadirListView
            .setOnClickListener{
                anadirEntrenador(adaptador)
            }
    }

    fun anadirEntrenador(
        adaptador: ArrayAdapter<BEntrenador>
    ){
        arreglo.add(
            BEntrenador(
                1,
                "Adrian",
                "Descriptionnnn"
            )
        )
        adaptador.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar -> {
                mostrarSnackbar("${posicionItemSeleccionado}")
                return true
            }
            R.id.mi_eliminar -> {
                mostrarSnackbar("${posicionItemSeleccionado}")
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(findViewById(R.id.lv_listView),
            texto,Snackbar.LENGTH_LONG)
        snack.show()
    }
}