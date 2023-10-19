package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1

        // Define la estructura de la tabla
        private const val TABLE_NAME = "Product"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, $COLUMN_DESCRIPTION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Si necesitas realizar alguna actualización en la estructura de la base de datos, puedes hacerlo aquí
        // Por simplicidad, no se realiza ninguna acción en este ejemplo
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }


    fun insertProduct(product: Product){
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_DESCRIPTION, product.description)

        }
        db.insert("Product", null, values)
        db.close()
    }

    fun readProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Product", null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val description= cursor.getString(cursor.getColumnIndex("description"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                products.add(Product(id,name, description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return products
    }
    fun updateProduct(product: Product){

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_DESCRIPTION, product.description)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(product.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getProductByID(productID: Int) : Product {
        val db = this.readableDatabase
        val query = "SELECT * FROM Product WHERE $COLUMN_ID = $productID"
        val cursor = db.rawQuery(query, null)
        var product = Product(0, "", "")
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val description= cursor.getString(cursor.getColumnIndex("description"))
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            product = Product(id,name, description)
        }
        cursor.close()
        db.close()
        return product
    }

    fun deleteProduct(productID: Int) {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(productID.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }


}