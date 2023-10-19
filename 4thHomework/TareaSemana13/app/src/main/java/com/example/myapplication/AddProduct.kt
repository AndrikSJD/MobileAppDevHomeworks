package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAddProductBinding

class AddProduct : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnAceptar.setOnClickListener {
            val name = binding.editTNombre.text.toString()
            val description = binding.descripcionProducto.text.toString()
            val product = Product(0, name, description)
            dbHelper.insertProduct(product)
            finish()
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
        }

        binding.btnCerrar.setOnClickListener {
            finish()
        }
    }

}