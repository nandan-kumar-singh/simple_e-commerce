package com.example.alsess.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {
    private val detailArrayList = ArrayList<ApiProductsModel>()
    val productMLD = MutableLiveData<List<ApiProductsModel>>()
    val loadMLD = MutableLiveData<Boolean>()
    val controlFavorites = MutableLiveData<Boolean>()
    val controlBasket = MutableLiveData<Boolean>()

    init {
        loadMLD.value = false
        controlFavorites.value = false
        controlBasket.value = false
    }

    fun loadProductData(id: Long) {
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData()
            .enqueue(object : Callback<List<ApiProductsModel>> {
                @SuppressLint("CheckResult")
                override fun onResponse(
                    call: Call<List<ApiProductsModel>>,
                    response: Response<List<ApiProductsModel>>,
                ) {
                    if (response.isSuccessful) {
                        var index = 0
                        while (index < response.body()!!.size) {
                            if (response.body()!!.get(index).id == id) {
                                detailArrayList.add(
                                    ApiProductsModel(
                                        response.body()!![index].id,
                                        response.body()!![index].title,
                                        response.body()!![index].price,
                                        response.body()!![index].description,
                                        response.body()!![index].category,
                                        response.body()!![index].image,
                                        response.body()!![index].rating
                                    )
                                )
                            }
                            index++
                        }
                        productMLD.value = detailArrayList
                        loadMLD.value = true
                    }
                }

                override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    fun controlFavoritesData(context: Context, id: Long) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()
        if (favoritesDAO.controlFavorites(favoritesDataHelper, id) == 0) {
            controlFavorites.value = true
        }
    }

    fun addFavoritesData(context: Context) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()
        favoritesDAO.addFavorites(
            favoritesDataHelper,
            detailArrayList[0].id,
            detailArrayList[0].title,
            detailArrayList[0].price,
            detailArrayList[0].image
        )
    }

    fun deleteFavorites(context: Context, id: Long) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()
        favoritesDAO.deleteFavorites(favoritesDataHelper, id)
    }

    fun controlBasketData(context: Context, id: Long) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketDAO = BasketSQLiteDao()
        if (basketDAO.controlBasket(basketDataHelper, id) == 0) {
            controlBasket.value = true
        }
    }

    fun addBasketData(context: Context) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketDAO = BasketSQLiteDao()
        basketDAO.addBasket(
            basketDataHelper,
            detailArrayList[0].id,
            detailArrayList[0].title,
            detailArrayList[0].price,
            detailArrayList[0].image
        )
    }

    fun deleteBasket(context: Context, id: Long) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketDAO = BasketSQLiteDao()
        basketDAO.deleteBasket(basketDataHelper, id)
    }
}