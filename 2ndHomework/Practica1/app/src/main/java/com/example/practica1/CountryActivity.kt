package com.example.practica1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class CountryActivity : AppCompatActivity() {

    private var history:String = "El usuario hizo uso de: "
    var paisSelected = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country)
        paisSelected = intent.getStringExtra("pais").toString()
        val tV:TextView = findViewById(R.id.txtV2)
        tV.text = "País seleccionado: $paisSelected"
        spinnerCountry()


    }

    fun exitActivity(view: View) {

        if (view.id == R.id.btnAceptar){
            val intent = Intent()
            intent.putExtra("history", history.substring(0, history.length - 2))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }


    private val countries:HashMap<String,Map<String,String>> = hashMapOf(
        "España" to hashMapOf(
            "Poblacion" to "Cien mil millones de habitantes",
            "Capital" to "Madrid",
            "Idioma" to "Español castellano"),
        "Japon" to hashMapOf(
            "Poblacion" to "Doscientos mil millones de habitantes",
            "Capital" to "Tokio",
            "Idioma" to "Japones"),
        "Estados Unidos" to hashMapOf(
            "Poblacion" to "Cincuenta mil millones de habitantes",
            "Capital" to "Washintong DC",
            "Idioma" to "Ingles"),
        "Italia" to hashMapOf(
            "Poblacion" to "Veinte mil millones de habitantes",
            "Capital" to "Roma",
            "Idioma" to "Italiano")

    )

    fun showInfo(paisSelected:String, infoOption:String){
        val et:TextView = findViewById(R.id.textVInfo)
        et.text = "Info: ${countries[paisSelected]!![infoOption]}"
        history += "$infoOption, "
    }

    fun spinnerCountry(){
        val elementos = listOf("Poblacion", "Capital", "Idioma")

        val spinner: Spinner = findViewById(R.id.spinner)

        // Crea un ArrayAdapter usando los elementos y el diseño predeterminado para el spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementos)

        // Especifica el diseño que se usará cuando se desplieguen las opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Une el ArrayAdapter al Spinner
        spinner.adapter = adapter

        // Opcionalmente, puedes configurar un escuchador para detectar la selección del usuario
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               showInfo(paisSelected, elementos[position])
                // Realiza alguna acción con el elemento seleccionado
                Toast.makeText(this@CountryActivity, "Seleccionaste: ${elementos[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Se llama cuando no se ha seleccionado nada en el Spinner (opcional)
                Toast.makeText(this@CountryActivity, "Nada", Toast.LENGTH_SHORT).show()
            }
        }
    }



}