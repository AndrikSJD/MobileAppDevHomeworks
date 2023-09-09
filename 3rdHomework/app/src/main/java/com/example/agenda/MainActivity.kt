package com.example.agenda

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var startForResult: ActivityResultLauncher<Intent>
    var listaEventos : MutableList<Event> = mutableListOf()
    var nombresEventos: MutableList<String> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Agregar eventos"
        listViewCountries()



        // Verifica si ya se ha mostrado el diálogo de configuración
        if (!configuracionInicial()) {
            // Si no se ha mostrado, muestra el diálogo de configuración
            primerLanzamientoApp()
        }

    }



    private fun configuracionInicial(): Boolean {
        // Obtener una referencia al objeto SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)
        // Comprueba si ya se ha mostrado el diálogo de configuración
        return sharedPreferences.getBoolean("DialogoConfiguracionMostrado", false)
    }


    private fun primerLanzamientoApp(){
         // Obtener una referencia al objeto SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        if (!sharedPreferences.contains("Almacenamiento")) {
            // La clave "almacenamiento" no existe en las preferencias es decir es la primera vez
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_preferences, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            val dialog = builder.create()
            val btnLocalmente: Button = dialogView.findViewById(R.id.btnLocal)
            btnLocalmente.setOnClickListener {
                editor.putBoolean("Almacenamiento", false)
                editor.putBoolean("DialogoConfiguracionMostrado", true) // Marca que el diálogo se ha mostrado
                editor.apply() // Guardar la configuración
                Toast.makeText(this@MainActivity, "Almacenamiento Local elegido", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            val btnExterno: Button = dialogView.findViewById(R.id.btnExterno)
            btnExterno.setOnClickListener {
                editor.putBoolean("Almacenamiento", true)
                editor.putBoolean("DialogoConfiguracionMostrado", true) // Marca que el diálogo se ha mostrado
                editor.apply() // Guardar la configuración
                Toast.makeText(this@MainActivity, "Almacenamiento Externo elegido", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            dialog.show()
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_option -> {
                mostrarDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




    fun mostrarDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_evento, null)


        var fechaSeleccionada : String= ""

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        val btnCerrar: Button = dialogView.findViewById(R.id.btnCerrarDialog)
        btnCerrar.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo cuando se hace clic en el botón "Cerrar"
        }

        val btnAceptar: Button = dialogView.findViewById(R.id.btnAceptarLayout)

        val calendarV: CalendarView = dialogView.findViewById(R.id.calendarView)

        calendarV.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Cuando el usuario selecciona una fecha, este código se ejecutará
          fechaSeleccionada = "$dayOfMonth/${month + 1}/$year" // Formatear la fecha como "dd/MM/yyyy"

        }

        btnAceptar.setOnClickListener {
            val tvnam: EditText = dialogView.findViewById(R.id.editTNombre)
            val tvdesc: EditText = dialogView.findViewById(R.id.descripcionEvento)
            val descripcion = tvdesc.text.toString()
            val nombre = tvnam.text.toString()

            saveEvent(nombre, descripcion, fechaSeleccionada, dialog)

        }

        dialog.show()
    }

    fun listViewCountries(){

        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)
        val esAlmacenamientoInterno = sharedPreferences.getBoolean("Almacenamiento", false)

        if (!esAlmacenamientoInterno) {
            val filename = "agenda_interna.txt"
            // Leer desde el almacenamiento interno
        try {
            val fileInputStream = openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val contenido = StringBuilder()
            var linea: String? = bufferedReader.readLine()
            while (linea != null) {
                contenido.append(linea).append("\n")
                linea = bufferedReader.readLine()
            }
            bufferedReader.close()

            // Intenta analizar el contenido como JSON
            // Crear un objeto Gson
            val gson = Gson()

            // Obtener el tipo de datos para la conversión
            val eventType = object : TypeToken<EventContainer>() {}.type

            // Convertir el JSON en un objeto EventContainer
            val eventContainer = gson.fromJson<EventContainer>(contenido.toString(), eventType)
            // Acceder a la lista de eventos
            listaEventos = eventContainer.eventList

        } catch (jsonException: JsonSyntaxException) {
            // Captura excepciones de análisis JSON inválido
            jsonException.printStackTrace()
        } catch (e: Exception) {
            // Captura otras excepciones
            e.printStackTrace()
        }
        } else {
            // Leer desde el almacenamiento externo
            val file = File(getExternalFilesDir(null), "agenda_externa.txt")

            try {
                val bufferedReader = BufferedReader(FileReader(file))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()
                val content = stringBuilder.toString()
                val gson = Gson()
                // Obtener el tipo de datos para la conversión
                val eventType = object : TypeToken<EventContainer>() {}.type

                // Convertir el JSON en un objeto EventContainer
                val eventContainer = gson.fromJson<EventContainer>(content.toString(), eventType)
                listaEventos = eventContainer.eventList
            } catch (jsonException: JsonSyntaxException) {
                // Captura excepciones de análisis JSON inválido
                jsonException.printStackTrace()
            } catch (e: Exception) {
                // Captura otras excepciones
                e.printStackTrace()
            }
        }

        val listView: ListView = findViewById(R.id.listViewEventos)


        //obtenemos los nombres de los eventos
        for (evento in listaEventos) {
            nombresEventos.add(evento.nombre)
        }


        // Crea un ArrayAdapter para mostrar los nombres en el ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresEventos)
        //Asocia el ArrayAdapter con el ListView

        listView.adapter = adapter
        // Configura un escuchador para el clic en los elementos del ListView
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                callActivity2( findViewById(R.id.listViewEventos),listaEventos[position])

            }
        }
    }


    fun callActivity2(view: View, evento: Event) {
        // Crear un Intent para iniciar la Activity2
        val intent = Intent(this, MainActivity2::class.java)

        Log.d("Etiqueta",evento.toString())

        intent.putExtra("nombre",evento.nombre)
        intent.putExtra("descripcion", evento.descripcion)
        intent.putExtra("fecha", evento.fecha)

        intent.putExtra("prueba", "probando123")

        startActivity(intent)
    }



    private fun saveEvent(nombre: String, descripcion: String, fechaSeleccionada: String, dialog: AlertDialog){

        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        val evento = Event(nombre,descripcion, fechaSeleccionada)

        listaEventos.add(evento)
        nombresEventos.add(nombre)

        val crrntEventList = EventContainer(listaEventos)
        val gson = Gson()
        val json = gson.toJson(crrntEventList)


        //Depediendo de las preferencias guardamos interna o externamente

        if(!sharedPreferences.getBoolean("Almacenamiento", false)) { //se guarda localmente


            val filename = "agenda_interna.txt"
            try {
                val outputStreamWriter = OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE))
                outputStreamWriter.write(json)
                outputStreamWriter.close()
                Toast.makeText(this@MainActivity, "Evento Guardado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else { //se guarda externamente

            val file = File(getExternalFilesDir(null), "agenda_externa.txt")

            try {
                val outputStream = FileOutputStream(file)
                outputStream.write(json.toByteArray())
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        adapter.notifyDataSetChanged()
        dialog.dismiss()
    }






}