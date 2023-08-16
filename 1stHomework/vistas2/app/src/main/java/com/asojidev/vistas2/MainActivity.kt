package com.asojidev.vistas2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerExample()
    }


    var itemSeleccionado: String = "";
    @SuppressLint("SetTextI18n")
    fun makeOperation(view:View){

        val edit1:EditText = findViewById(R.id.editText)
        val edit2:EditText = findViewById(R.id.editText2)
        val tvResultado: TextView = findViewById(R.id.tvResultado)

        if (edit1.text.toString() != "" && edit2.text.toString() != ""){

            val valor1 = edit1.text.toString().toInt()
            val valor2 = edit2.text.toString().toInt()


            val resultado: Int = when (itemSeleccionado) {
                "Suma" -> (valor1 + valor2)
                "Resta" -> (valor1 - valor2)
                "Division" -> (valor1 / valor2)
                "Multiplicacion" -> (valor1 * valor2)
                else -> {
                    -1
                }
            }

            if (resultado != -1){
                tvResultado.text = "Resultado: $resultado"
            } else{
                Toast.makeText(this@MainActivity, "Por favor, seleccione una operacion", Toast.LENGTH_SHORT).show()
            }

        } else{
            Toast.makeText(this@MainActivity, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show()
        }

    }

    fun spinnerExample() {

        //obtener array de los recursos
        val elementos2 = resources.getStringArray(R.array.operationsArray)

        val spinner: Spinner = findViewById(R.id.spinner)

        // Crea un ArrayAdapter usando los elementos y el diseño predeterminado para el spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementos2)

        // Especifica el diseño que se usará cuando se desplieguen las opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Une el ArrayAdapter al Spinner
        spinner.adapter = adapter

        // Opcionalmente, puedes configurar un escuchador para detectar la selección del usuario
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSeleccionado = elementos2[position]
                // Realiza alguna acción con el elemento seleccionado
                Toast.makeText(this@MainActivity, "Seleccionaste: $itemSeleccionado", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Se llama cuando no se ha seleccionado nada en el Spinner (opcional)
                Toast.makeText(this@MainActivity, "Nada", Toast.LENGTH_SHORT).show()
            }
        }
    }

}