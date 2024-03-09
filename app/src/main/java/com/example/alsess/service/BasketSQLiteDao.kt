package com.example.alsess.service

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.alsess.model.BasketSQLiteModel

class BasketSQLiteDao {
    fun addBasket(basketDataHelper: BasketSQLiteDataHelper, id : Long, title: String, price: Double, image_url : String, count : Int = 1) {
        val db = basketDataHelper.writableDatabase
        val values = ContentValues()
        values.put("id",id)
        values.put("title", title)
        values.put("price", price)
        values.put("image_url", image_url)
        values.put("count", count)

        db.insertOrThrow("basket", null, values)
        db.close()
    }

    fun updateBasket(
        basketDataHelper: BasketSQLiteDataHelper,
        id: Long,
        title: String,
        price: Double,
        imageUrl: String,
        count : Int
    ) {
        val db = basketDataHelper.writableDatabase
        val values = ContentValues()
        values.put("id",id)
        values.put("title",title)
        values.put("price",price)
        values.put("image_url",imageUrl)
        values.put("count",count)

        db.update("basket",values,"id=?", arrayOf(id.toString()))
        db.close()


    }

    @SuppressLint("Range")
    fun getBasket(basketDataHelper: BasketSQLiteDataHelper, id: Long): BasketSQLiteModel? {
        val db = basketDataHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM basket WHERE id='$id'", null)
        while (cursor.moveToNext()) {
            return BasketSQLiteModel(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getDouble(cursor.getColumnIndex("price")),
                cursor.getString(cursor.getColumnIndex("image_url")),
                cursor.getInt(cursor.getColumnIndex("count"))
            )
        }
        return null;
    }

    @SuppressLint("Range")
    fun getAllBaskets(basketDataHelper: BasketSQLiteDataHelper): ArrayList<BasketSQLiteModel> {
        val basketArrayList = ArrayList<BasketSQLiteModel>()
        val db = basketDataHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM basket", null)
        while (cursor.moveToNext()) {
            val basket = BasketSQLiteModel(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getDouble(cursor.getColumnIndex("price")),
                cursor.getString(cursor.getColumnIndex("image_url")),
                cursor.getInt(cursor.getColumnIndex("count"))
            )
            basketArrayList.add(basket)
        }

        return basketArrayList
    }
    fun deleteBasket(basketDataHelper: BasketSQLiteDataHelper, id: Long){
        val db = basketDataHelper.writableDatabase
        db.delete("basket","id=?", arrayOf(id.toString()))
        db.close()
    }
    @SuppressLint("Range", "Recycle")
    fun controlBasket(basketDataHelper : BasketSQLiteDataHelper, id : Long) : Int {
        var conclusion = 0
        val db = basketDataHelper.writableDatabase
        val cursor = db.rawQuery("SELECT count(*) AS conclusion FROM basket WHERE id='$id'",null)
        while(cursor.moveToNext()){
            conclusion = cursor.getInt(cursor.getColumnIndex("conclusion"))
        }

        return conclusion

    }
}