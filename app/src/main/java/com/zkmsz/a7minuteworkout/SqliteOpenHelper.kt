package com.zkmsz.a7minuteworkout

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteOpenHelper(context: Context,factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1 //This DataBase version
        private val DATABASE_NAME= "SevenMinutesWorkout.db" //Name of DATABASE
        private val TAPLE_HISTORY = "history" //Table name
        private val COLUMN_ID= "_id" //Column id
        private val COLUMN_COMPLETED_DATE= "completed_date" //Column_date
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //CREATE TABLE history (_id INTEGER PRIMARY KEY, completed_date TEXT
        val CREATE_EXERCISE_TABLE= ("CREATE TABLE "
                + TAPLE_HISTORY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_COMPLETED_DATE + " TEXT)")
        db?.execSQL(CREATE_EXERCISE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS" + TAPLE_HISTORY)
        onCreate(db)
    }

    fun addDate(date: String)
    {
        val values = ContentValues()
        values.put(COLUMN_COMPLETED_DATE,date)

        val db= this.writableDatabase
        db.insert(TAPLE_HISTORY,null,values)
        db.close()
    }

    fun getAllComlpletedDateList(): ArrayList<String>{

        val list = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TAPLE_HISTORY", null)
        while (cursor.moveToNext())
        {
            val dateValue= (cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_DATE)))
            list.add(dateValue)
        }

        cursor.close()

        return list

    }
}