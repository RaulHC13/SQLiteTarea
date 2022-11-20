package com.example.tareasqlite_herreriascorralraul.bd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tareasqlite_herreriascorralraul.models.articuloModel

class BaseDatos (context: Context): SQLiteOpenHelper(context, BD, null, VERSION) {

    companion object {
        const val BD = "articulos_bd"
        const val VERSION = 1
        const val TABLA = "Articulos"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLA(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE," +
                "precio FLOAT NOT NULL," +
                "stock INTEGER)"

        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val query = "DROP TABLE IF EXISTS $TABLA"
        p0?.execSQL(query)
        onCreate(p0)
    }

    //CRUD

    fun crear(articulo: articuloModel): Long { //INSERT
        //Conexion
        val con = this.writableDatabase //writableDatabase para escribir

        val valores = ContentValues().apply { //Funciona como un bundle para BD
            put("NOMBRE", articulo.nombre)
            put("PRECIO", articulo.precio)
            put("STOCK", articulo.stock)
        }

        val cod = con.insert(TABLA, null, valores)
        con.close()
        return cod //Si no se ha podido hacer el insert, cod es -1
    }

    @SuppressLint("Range") //El id da una advertencia de que puede ser -1. pero es autoincrement desde 0 no puede llegar a ser -1.
    fun read(): MutableList<articuloModel> {
        val lista = mutableListOf<articuloModel>()
        val con = this.readableDatabase //readableDatabase para leer
        val query = "SELECT * FROM $TABLA ORDER BY id"

        try {
        val cursor = con.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val articulo = articuloModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getFloat(cursor.getColumnIndex("precio")),
                    cursor.getInt(cursor.getColumnIndex("stock"))
                )
                lista.add(articulo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
        con.close()
        return lista
    }

    fun borrar(id: Int?) {
        val query = "DELETE FROM $TABLA WHERE id = $id"
        val con = this.writableDatabase
        con.execSQL(query)
        con.close()
    }

    fun update(articulo: articuloModel): Int {

        var con = this.writableDatabase
        val valores = ContentValues().apply {
            put ("NOMBRE", articulo.nombre)
            put ("PRECIO", articulo.precio)
            put ("STOCK", articulo.stock)
        }

        val update = con.update(TABLA, valores, "id = ?", arrayOf(articulo.id.toString())) //Se utiliza "id = ?"
        con.close()
        return update
    }

    fun existeNombre(nombre: String, id:Int?): Boolean {

        val query = if (id == null) {
            "SELECT id FROM $TABLA WHERE nombre = '$nombre'"
        }
        else {
            "SELECT id FROM $TABLA WHERE nombre = '$nombre' AND id != $id"
        }

        val con = this.readableDatabase
        var filas = 0

        try {

            val cursor = con.rawQuery(query, null)
            filas = cursor.count //El número de filas que devuelve la query
            cursor.close()

        } catch (e: Exception){
            e.printStackTrace()
        }
        return (filas != 0) //Si existe alguna fila, existe el nombre en la BD.
    }
    fun borrarTodo() {
        val query = "DELETE FROM $TABLA"
        val con = this.writableDatabase
        con.execSQL(query)
        con.close()
    }
}