package com.example.alsess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.ProductsRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductCategoryViewModel : ViewModel() {
    private val productArrayList = ArrayList<ApiProductsModel>()
    val productMLD = MutableLiveData<List<ApiProductsModel>>()
    val productLoadMLD = MutableLiveData<Boolean>()

    fun productLoadData(category: String) {
        val retrofit = ProductsRetrofitService
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {

                if (response.isSuccessful) {
                    productArrayList.addAll(response.body()!!.filter { it.category == category }
                        .map {
                            ApiProductsModel(
                                it.id,
                                it.title,
                                it.price,
                                it.description,
                                it.category,
                                it.image,
                                it.rating
                            )
                        })
                    productMLD.value = productArrayList
                    productLoadMLD.value = true
                }
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}