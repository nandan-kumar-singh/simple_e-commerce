package com.example.alsess.service

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class FavoritesSQLiteDataHelper(context : Context) : SQLiteOpenHelper(context,"Favorites",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE favorites (id INTEGER, title TEXT ,price REAL ,image_url TEXT );")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE İF EXİSTS favorites")
        onCreate(db!!)
    }
}