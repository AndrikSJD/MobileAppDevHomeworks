package com.example.practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listViewCountries()

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val resultado = data?.getStringExtra("history")
                // Hacer algo con el resultado recibido de Activity3
                Toast.makeText(this@MainActivity, "Resultado: $resultado", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun listViewCountries(){
        val countries = resources.getStringArray(R.array.countriesList)
        val listView: ListView = findViewById(R.id.listView1)
    // Crea un ArrayAdapter para mostrar los nombres en el ListView
    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
    //Asocia el ArrayAdapter con el ListView

        listView.adapter = adapter
    // Configura un escuchador para el clic en los elementos del ListView
    listView.onItemClickListener = object : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    callActivityCountry( findViewById(R.id.listView1),countries[position])
    // Muestra un Toast con el nombre seleccionado
            }
        }
    }
    fun callActivityCountry(view: View, pais:String){
        // Crear un Intent para iniciar la Activity2
        val intent = Intent(this, CountryActivity::class.java)

        // Opcional: Puedes enviar datos extras a la Activity2 utilizando putExtra
        intent.putExtra("pais", pais)

        // Iniciar la Activity2 utilizando el Intent
        startForResult.launch(intent)
    }
    }
