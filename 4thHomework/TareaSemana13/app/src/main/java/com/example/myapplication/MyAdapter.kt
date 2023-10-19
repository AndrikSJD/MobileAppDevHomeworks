package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val context: Context, private var data: List<Product>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private val dbHelper = DatabaseHelper(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvProductDescription)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteBtn)
        val editBtn: Button = itemView.findViewById(R.id.editBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tvName.text = "Nombre: ${item.name}"
        holder.tvDesc.text = "Descripcion: ${item.description}"

        holder.editBtn.setOnClickListener {

            val intent = Intent(holder.itemView.context, EditProduct::class.java).apply {
                putExtra("product_id", item.id)
            }
            holder.itemView.context.startActivity(intent)

        }
        holder.deleteBtn.setOnClickListener {

            dbHelper.deleteProduct(item.id)
            refreshData(dbHelper.readProducts())
            Toast.makeText(holder.itemView.context, "Producto eliminado", Toast.LENGTH_SHORT).show()

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData(newNotes: List<Product>) {
        data = newNotes
        notifyDataSetChanged()
    }





}