package com.example.alsess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.ProductsRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductSearchViewModel : ViewModel() {
    private val productArrayList = ArrayList<ApiProductsModel>()
    val productMLD = MutableLiveData<List<ApiProductsModel>>()
    val productLoadMLD = MutableLiveData<Boolean>()

    fun productLoadData(category: String) {
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {
                if (response.isSuccessful) {
                    var index = 0
                    while (index < response.body()!!.size) {
                        if (response.body()!![index].category.contains(category)) {
                            productArrayList.add(
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