package com.example.agenda

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val nombre = intent.getStringExtra("nombre")
        val descripcion = intent.getStringExtra("descripcion")
        val fecha = intent.getStringExtra("fecha")
        val tVName: TextView = findViewById(R.id.tvNombre)
        val tVDescription: TextView = findViewById(R.id.tvDescripcion)
        val tVF: TextView = findViewById(R.id.tvFecha)

        tVName.text = "Nombre: $nombre"
        tVDescription.text = "Descripcion : $descripcion"
        tVF.text = "Fecha: $fecha"


    }




    fun exitActivity(view: View) {
        finish()
    }
}