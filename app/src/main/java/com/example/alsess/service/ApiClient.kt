package com.example.alsess.service

import com.example.alsess.model.ApiProductsModel
import javax.inject.Inject

class ApiClient @Inject constructor(private val service: ProductsAPI) {
    suspend fun getProducts(): List<ApiProductsModel>? {
        val data = service.loadData()
        return null
    }
}