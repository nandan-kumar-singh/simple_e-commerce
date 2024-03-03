package com.example.alsess.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aaaaaa.ProductParentAdapter
import com.example.alsess.service.ProductsRetrofitService
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductChildBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.model.ProductRVChildModel
import com.example.alsess.model.ProductRVParentModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductChildBinding
    val productParentList = ArrayList<ProductRVParentModel>()
    val productChildList = ArrayList<ProductRVChildModel>()
    val productChildList2 = ArrayList<ProductRVChildModel>()
    val productChildList3 = ArrayList<ProductRVChildModel>()
    val productChildList4 = ArrayList<ProductRVChildModel>()

    /*Clicking the radiobutton in the product search and categories section is saved,
     when you come to the home page,
      this record is deleted and returns to the default values
     */
    override fun onStart() {
        super.onStart()
        val sharedPreferences =
            context!!.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProductChildBinding.inflate(inflater, container, false)
        productLoadData()
        viewBinding.fragmentProductChildRecyclerView.adapter =
            context?.let { ProductParentAdapter(it, productParentList) }
        viewBinding.fragmentProductChildRecyclerView.layoutManager =
            LinearLayoutManager(context)
        if (productParentList.size != 0) {
            viewBinding.fragmentProductChildPgb.visibility = View.GONE
        }

        //Click on the search button and it switches to the search fragment
        viewBinding.fragmentProductChildSearchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_productChildFragment_to_searchFragment)
            }
        }

        return viewBinding.root
    }

    fun productLoadData() {
        val retrofit = ProductsRetrofitService()
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
                            context?.let { ProductParentAdapter(it, productParentList) }
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
            ProductRVParentModel(
                getString(R.string.mansCloting), "men's clothing", productChildList
            )
        )
        productParentList.add(
            ProductRVParentModel(
                getString(R.string.womensClothing), "women's clothing", productChildList2
            )
        )
        productParentList.add(
            ProductRVParentModel(
                getString(R.string.jewelery), "jewelery", productChildList3
            )
        )
        productParentList.add(
            ProductRVParentModel(
                getString(R.string.electronics), "electronics", productChildList4
            )
        )

    }

    fun whileLoopNestedRV(
        childList: ArrayList<ProductRVChildModel>,
        category: String,
        response: Response<List<ApiProductsModel>>
    ) {
        var indeks = 0
        var element = 0

        while (indeks < response.body()!!.size) {
            if (response.body()!!.get(indeks).category == category) {
                if (element < 4) {
                    childList.add(
                        ProductRVChildModel(
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