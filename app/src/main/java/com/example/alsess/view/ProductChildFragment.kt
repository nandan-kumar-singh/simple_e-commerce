package com.example.alsess.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aaaaaa.ProductParentAdapter
import com.example.alsess.ProductsRetrofit
import com.example.alsess.R
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.databinding.FragmentProductChildBinding
import com.example.alsess.recyclerviewmodel.ProductChildModel
import com.example.alsess.recyclerviewmodel.ProductParentModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductChildBinding
    val productParentList = ArrayList<ProductParentModel>()
    val productChildList = ArrayList<ProductChildModel>()
    val productChildList2 = ArrayList<ProductChildModel>()
    val productChildList3 = ArrayList<ProductChildModel>()
    val productChildList4 = ArrayList<ProductChildModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProductChildBinding.inflate(inflater, container, false)
        productLoadData()
        viewBinding.fragmentProductChildRecyclerView.adapter =
            ProductParentAdapter(requireContext(), productParentList)
        viewBinding.fragmentProductChildRecyclerView.layoutManager =
            LinearLayoutManager(context)
        if (productParentList.size != 0) {
            viewBinding.fragmentProductChildPgb.visibility = View.GONE
        }
        return viewBinding.root
    }

    fun productLoadData() {
        val retrofit = ProductsRetrofit()
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {
                if (response.isSuccessful) {
                    viewBinding.fragmentProductChildPgb.visibility = View.GONE

                    addDataNestedRecyclerView(response)
                    if (productParentList.size == 0) {
                        viewBinding.fragmentProductChildRecyclerView.adapter =
                            ProductParentAdapter(requireContext(), productParentList)
                        viewBinding.fragmentProductChildRecyclerView.layoutManager =
                            LinearLayoutManager(context)
                        addDataParentList()
                    }
                }
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    //Adds to parentLi
    fun addDataParentList() {
        productParentList.add(
            ProductParentModel(
                getString(R.string.mansCloting), "men's clothing", productChildList
            )
        )
        productParentList.add(
            ProductParentModel(
                getString(R.string.womensClothing), "women's clothing", productChildList2
            )
        )
        productParentList.add(
            ProductParentModel(
                getString(R.string.jewelery), "jewelery", productChildList3
            )
        )
        productParentList.add(
            ProductParentModel(
                getString(R.string.electronics), "electronics", productChildList4
            )
        )

    }

    fun whileLoopNestedRV(
        childList: ArrayList<ProductChildModel>,
        category: String,
        response: Response<List<ApiProductsModel>>
    ) {
        var indeks = 0
        var element = 0

        while (indeks < response.body()!!.size) {
            if (response.body()!!.get(indeks).category == category) {
                if (element < 4) {
                    childList.add(
                        ProductChildModel(
                            response.body()!!.get(indeks).id,
                            response.body()!!.get(indeks).title,
                            response.body()!!.get(indeks).image,
                            response.body()!!.get(indeks).price,
                            response.body()!!.get(indeks).rating.rate
                        )
                    )

                    element++
                }

            }
            indeks++
        }
    }

    //Add to childLists and select category
    fun addDataNestedRecyclerView(response: Response<List<ApiProductsModel>>) {
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