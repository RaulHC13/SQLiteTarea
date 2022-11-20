package com.example.tareasqlite_herreriascorralraul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.tareasqlite_herreriascorralraul.adapters.ArticulosAdapter
import com.example.tareasqlite_herreriascorralraul.bd.BaseDatos
import com.example.tareasqlite_herreriascorralraul.databinding.ActivityMainBinding
import com.example.tareasqlite_herreriascorralraul.models.articuloModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var con: BaseDatos
    lateinit var adapter: ArticulosAdapter
    var lista = mutableListOf<articuloModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        con = BaseDatos(this)

        setListeners()
        setRecycle()
    }

    private fun setRecycle() {
        lista = con.read()

        binding.tvSinArticulos.visibility = View.INVISIBLE

        if (lista.size == 0) {
            binding.tvSinArticulos.visibility = View.VISIBLE
            return
        }

        var layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = ArticulosAdapter(lista, {onItemDelete(it)}, {onItemUpdate(it)})
    //Son 2 funciones lambda, la segunda se puede sacar fuera y escribir normal: articulo -> onItemUpgrade(articulo)
        binding.recyclerView.adapter = adapter
    }

    private fun onItemUpdate(it: articuloModel) {

        val i = Intent(this, ActivityRegistrar::class.java).apply {
            putExtra("ARTICULO", it)
        }
        startActivity(i)
    }

    private fun onItemDelete(it: Int) { //it es la posición
        //Borrar de la BD
        val articulo = lista[it]
        con.borrar(articulo.id)

        //Borrar del recycler para que se refleje en la aplicación
        lista.removeAt(it)
        if (lista.size == 0) { //Si no hay más articulos se muestra el tv
            binding.tvSinArticulos.visibility = View.VISIBLE
        }
        adapter.notifyItemRemoved(it)
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, ActivityRegistrar::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        setRecycle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //Switch en el return, siempre devuelve true, hay que indicarlo en cada caso.
            R.id.menuAdd -> {
                startActivity(Intent(this, ActivityRegistrar::class.java))
                true
            }

            R.id.menuBorrar -> {
                con.borrarTodo()
                lista.clear()
                adapter.notifyDataSetChanged()
                binding.tvSinArticulos.visibility = View.VISIBLE
                true
            }

            R.id.menuSalir -> {
                finish()
                true
            }

            else -> true //Siempre devuelve true
        }
    }
}