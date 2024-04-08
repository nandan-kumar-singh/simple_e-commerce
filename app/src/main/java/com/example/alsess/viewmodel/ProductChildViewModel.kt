package com.example.alsess.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.R
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.model.ProductRVChildModel
import com.example.alsess.model.ProductRVParentModel
import com.example.alsess.service.ProductsAPI
import com.example.alsess.service.ProductsRetrofitService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProductChildViewModel : ViewModel() {
    val producParenttMLD = MutableLiveData<List<ProductRVParentModel>>()
    val loadMLD = MutableLiveData<Boolean>()
    private val productParentList = ArrayList<ProductRVParentModel>()
    private val productChildList = ArrayList<ProductRVChildModel>()
    private val productChildList2 = ArrayList<ProductRVChildModel>()
    private val productChildList3 = ArrayList<ProductRVChildModel>()
    private val productChildList4 = ArrayList<ProductRVChildModel>()


    fun productLoadData(context: Context) {
        val retrofit = ProductsRetrofitService
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {
                if (response.isSuccessful) {
                    loadMLD.value = true
                    addDataNestedRecyclerView(response)
                    addDataParentList(context)

                }
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    //Adds to parentLi
    private fun addDataParentList(context : Context) {
        productParentList.add(
            ProductRVParentModel(
                context.getString(R.string.mansCloting), "men's clothing", productChildList
            )
        )
        productParentList.add(
            ProductRVParentModel(
                context.getString(R.string.womensClothing), "women's clothing", productChildList2
            )
        )
        productParentList.add(
            ProductRVParentModel(
                context.getString(R.string.jewelery), "jewelery", productChildList3
            )
        )
        productParentList.add(
            ProductRVParentModel(
                context.getString(R.string.electronics), "electronics", productChildList4
            )
        )
        producParenttMLD.value = productParentList
    }

    private fun whileLoopNestedRV(
        childList: ArrayList<ProductRVChildModel>,
        category: String,
        response: Response<List<ApiProductsModel>>
    ) {
        val data = response.body()
        val filteredData = data?.filter { it.category == category }?.map {
            ProductRVChildModel(
                it.id,
                it.title,
                it.image,
                it.price,
                it.rating.rate
            )
        }
        filteredData?.let {
            childList.addAll(filteredData)
        }
    }

    //Add to childLists and select category
    private fun addDataNestedRecyclerView(response: Response<List<ApiProductsModel>>) {
        if (productChildList.size == 0) {
            whileLoopNestedRV(
                productChildList,
                "men's clothing", response
            )
            whileLoopNestedRV(
                productChildList2,
                "women's clothing", response
            )
            whileLoopNestedRV(
                productChildList3,
                "jewelery", response
            )
            whileLoopNestedRV(
                productChildList4,
                "electronics", response
            )
        }
    }
}