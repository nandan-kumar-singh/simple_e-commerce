package com.example.alsess.apis

import com.example.alsess.ApiLinks
import com.example.alsess.apimodels.ApiProductsModel
import retrofit2.Call
import retrofit2.http.GET

interface ProductsAPI {
    @GET(ApiLinks.PRODUCTS_CONTİNUANCE_URL)
    fun loadData() : Call<List<ApiProductsModel>>
}