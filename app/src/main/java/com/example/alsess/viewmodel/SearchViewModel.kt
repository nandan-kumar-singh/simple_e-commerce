package com.example.alsess.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.ProductsAPI
import com.example.alsess.service.ProductsRetrofitService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.collections.HashSet

class SearchViewModel : ViewModel() {
    private val searchItemList = HashSet<String>()
    val searchItemMLD = MutableLiveData<HashSet<String>>()

    fun searchItemLoadData() {
        val retrofit = ProductsRetrofitService
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {
                if (response.isSuccessful) {
                    var index = 0
                    while (index < response.body()!!.size) {
                        searchItemList.add(response.body()!![index].title)
                        index++
                    }
                }
                searchItemMLD.value = searchItemList
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}