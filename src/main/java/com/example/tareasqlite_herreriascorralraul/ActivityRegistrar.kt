package com.example.tareasqlite_herreriascorralraul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tareasqlite_herreriascorralraul.bd.BaseDatos
import com.example.tareasqlite_herreriascorralraul.databinding.ActivityRegistrarBinding
import com.example.tareasqlite_herreriascorralraul.models.articuloModel

class ActivityRegistrar : AppCompatActivity() {

    lateinit var binding: ActivityRegistrarBinding

    var id: Int? = null
    var nombre = ""
    var precio = 0.0F
    var stock = 0
    lateinit var con: BaseDatos
    var editando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        con = BaseDatos(this)
        setContentView(binding.root)
        cogerDatos()
        setListeners()
    }

    private fun cogerDatos() {
        val datos = intent.extras

        if (datos != null) {
            editando = true
            binding.btnAdd.text = "EDITAR"

            val articulo = datos.getSerializable("ARTICULO") as articuloModel

            id = articulo.id

            binding.etNombre.setText(articulo.nombre)
            binding.etPrecio.setText(articulo.precio.toString())
            binding.etStock.setText(articulo.stock.toString())

        }
    }

    private fun setListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            crearArticulo()
        }
    }

    private fun crearArticulo() {
        nombre = binding.etNombre.text.toString()
        precio = 0.0F
        stock = 0

        if (nombre.trim().isEmpty()) {
            Toast.makeText(this, "El campo nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
            binding.etNombre.requestFocus()
            binding.etNombre.setError("Nombre no puede estar vacio")
            return
        }

        if (con.existeNombre(nombre, id)){
            Toast.makeText(this, "Introduce otro nombre", Toast.LENGTH_SHORT).show()
            binding.etNombre.requestFocus()
            binding.etNombre.setError("Nombre ya existe")
            return
        }


        if (binding.etPrecio.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Debes añadir un precio", Toast.LENGTH_SHORT).show()
            binding.etPrecio.requestFocus()
            binding.etPrecio.setError("Añade un precio")
            return

        } else if (binding.etPrecio.text.toString().trim().isNotEmpty()) {
           precio = binding.etPrecio.text.toString().toFloat()
            if(precio <= 0.0) {
                Toast.makeText(this, "El precio debe ser mayor que 0", Toast.LENGTH_SHORT).show()
                binding.etPrecio.requestFocus()
                binding.etPrecio.setError("Precio debe ser mayor que 0")
                return
            }
        }


        if (binding.etStock.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Debes añadir un stock", Toast.LENGTH_SHORT).show()
            binding.etStock.requestFocus()
            binding.etStock.setError("Añade un stock")
            return

        } else if (binding.etStock.text.toString().trim().isNotEmpty()) {
            stock = binding.etStock.text.toString().toInt()
            if (stock <= 0) {
                Toast.makeText(this, "El stock debe ser mayor que 0", Toast.LENGTH_SHORT).show()
                binding.etStock.requestFocus()
                binding.etStock.setError("Stock debe ser mayor que 0")
                return
            }
        }

        if (!editando) {
            val articulo = articuloModel(1, nombre,precio,stock)

            if(con.crear(articulo) >-1) {
            finish()
            }else {
                Toast.makeText(this, "No se ha podido crear el registro", Toast.LENGTH_SHORT).show()
            }

        } else {
            val articulo = articuloModel(id,nombre,precio,stock)
            if (con.update(articulo) >-1) {
                finish()
            }else {
                Toast.makeText(this, "No se ha podido crear el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }
}