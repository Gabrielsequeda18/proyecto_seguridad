package com.example.app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        factory,
        DATABASE_VERSION
    ) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_EMAIL TEXT, $COLUMN_UUID TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

    }

    fun insertRow(email: String, uuid: String){

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_UUID, uuid)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun updateRow(email: String, uuid: String){
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_UUID, uuid)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteRow(row_id: String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun getAllRow(): Cursor?{

        val db = this.readableDatabase
        return  db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }


    companion object {
        const val DATABASE_VERSION = 1
        const  val DATABASE_NAME = "security.db"
        const  val TABLE_NAME = "user"
        const val COLUMN_ID = "id"
        const  val COLUMN_EMAIL = "email"
        const  val COLUMN_UUID = "uuid"
    }

}