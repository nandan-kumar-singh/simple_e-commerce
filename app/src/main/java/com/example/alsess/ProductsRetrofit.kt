package com.example.alsess

import com.example.alsess.apis.ProductsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsRetrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl(ApiLinks.PRODUCTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ProductsAPI:: class.java)
}