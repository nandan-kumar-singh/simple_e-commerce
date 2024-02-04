package com.example.alsess.sqlitedaos

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import com.example.alsess.sqlitemodels.SqliteFavoritesModel


class FavoritesSqliteDao {

    fun addFavorites(favoritesDataHelper: FavoritesSqliteDataHelper, id : Long, title: String, price: Double, image_url: String) {
        val db = favoritesDataHelper.writableDatabase
        val values = ContentValues()
        values.put("id",id)
        values.put("title", title)
        values.put("price", price)
        values.put("image_url", image_url)

        db.insertOrThrow("favorites", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun readFavorites(favoritesDataHelper: FavoritesSqliteDataHelper): ArrayList<SqliteFavoritesModel> {
        val favoritesArrayList = ArrayList<SqliteFavoritesModel>()
        val db = favoritesDataHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM favorites", null)
        while (cursor.moveToNext()) {
            val product = SqliteFavoritesModel(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getDouble(cursor.getColumnIndex("price")),
                cursor.getString(cursor.getColumnIndex("image_url"))
            )
            favoritesArrayList.add(product)
        }

        return favoritesArrayList
    }
    fun deleteFavorites(favoritesDataHelper : FavoritesSqliteDataHelper, id : Long){
        val db = favoritesDataHelper.writableDatabase
        db.delete("favorites","id=?", arrayOf(id.toString()) )
        db.close()
    }
    @SuppressLint("Range", "Recycle")
    fun controlFavorites(favoritesDataHelper : FavoritesSqliteDataHelper, id : Long) : Int {
        var conclusion = 0
        val db = favoritesDataHelper.writableDatabase
        val cursor = db.rawQuery("SELECT count(*) AS conclusion FROM favorites WHERE id='$id'",null)
        while(cursor.moveToNext()){
            conclusion = cursor.getInt(cursor.getColumnIndex("conclusion"))
        }

        return conclusion

    }
}