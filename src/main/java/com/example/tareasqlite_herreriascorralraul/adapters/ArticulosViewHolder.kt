package com.example.tareasqlite_herreriascorralraul.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tareasqlite_herreriascorralraul.databinding.ArticulosLayoutBinding
import com.example.tareasqlite_herreriascorralraul.models.articuloModel

class ArticulosViewHolder(v: View): RecyclerView.ViewHolder(v) {

    var binding = ArticulosLayoutBinding.bind(v)

    fun render(articulo: articuloModel, onItemDelete: (Int) -> Unit, onItemUpdate: (articuloModel) -> Unit) {
        binding.tvID.text = articulo.id.toString()
        binding.tvNombre.text = articulo.nombre
        binding.tvPrecio.text = articulo.precio.toString()
        binding.tvStock.text = articulo.stock.toString()

        binding.btnElim.setOnClickListener {
            onItemDelete(adapterPosition) //adapterPosition da la posición del adaptador.
        }

        itemView.setOnClickListener{
            onItemUpdate(articulo) //Al hacer click en un articulo se abre el menú para editarlo.
        }
    }
}