package com.example.tareasqlite_herreriascorralraul.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tareasqlite_herreriascorralraul.R
import com.example.tareasqlite_herreriascorralraul.models.articuloModel

class ArticulosAdapter(private val lista: MutableList<articuloModel>,
                       private val onItemDelete: (Int)-> Unit,
                       private val onItemUpdate: (articuloModel)-> Unit) //Función lambda como parámetro.
                        :RecyclerView.Adapter<ArticulosViewHolder>() { //Se extiende

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticulosViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.articulos_layout, parent, false)
        return ArticulosViewHolder(v)
    }

    override fun onBindViewHolder(holder: ArticulosViewHolder, position: Int) {
        holder.render(lista[position], onItemDelete, onItemUpdate)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}