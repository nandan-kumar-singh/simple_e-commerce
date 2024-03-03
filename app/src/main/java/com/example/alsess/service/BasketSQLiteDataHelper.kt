package com.example.alsess.service

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BasketSQLiteDataHelper(context : Context) : SQLiteOpenHelper(context,"Basket",null,1)  {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE basket (id INTEGER, title TEXT ,price REAL ,image_url TEXT,count INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE İF EXİSTS basket")
        onCreate(db)
    }
}