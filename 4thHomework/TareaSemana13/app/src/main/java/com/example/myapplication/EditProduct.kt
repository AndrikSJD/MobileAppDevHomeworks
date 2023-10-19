package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityEditProductBinding

class EditProduct : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductBinding
    private lateinit var dbHelper: DatabaseHelper
    private var productId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        productId = intent.getIntExtra("product_id", -1)
        if(productId != -1){
            val product = dbHelper.getProductByID(productId)
            binding.editTNombre.setText(product.name)
            binding.descripcionProducto.setText(product.description)

            binding.btnAceptar.setOnClickListener {
                val name = binding.editTNombre.text.toString()
                val description = binding.descripcionProducto.text.toString()
                val updatedProduct = Product(productId, name, description)
                dbHelper.updateProduct(updatedProduct)
                finish()
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
            }


        } else {
            finish()
            return
        }
    }
}