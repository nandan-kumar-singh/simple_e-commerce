package com.example.alsess.viewmodel

import android.view.View
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
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {

                if (response.isSuccessful) {

                    var indeks = 0
                    while (indeks < response.body()!!.size) {
                        if (category == response.body()!!.get(indeks).category) {
                            productArrayList.add(
                                ApiProductsModel(
                                    response.body()!!.get(indeks).id,
                                    response.body()!!.get(indeks).title,
                                    response.body()!!.get(indeks).price,
                                    response.body()!!.get(indeks).description,
                                    response.body()!!.get(indeks).category,
                                    response.body()!!.get(indeks).image,
                                    response.body()!!.get(indeks).rating
                                )
                            )
                        }
                        indeks++
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