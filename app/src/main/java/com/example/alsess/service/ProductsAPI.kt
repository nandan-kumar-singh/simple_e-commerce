package com.example.alsess.service

import com.example.alsess.model.ApiProductsModel
import retrofit2.Call
import retrofit2.http.GET

interface ProductsAPI {

    @GET("products")
    fun loadData() : Call<List<ApiProductsModel>>
}