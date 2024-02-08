package com.example.alsess.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.ProductsRetrofit
import com.example.alsess.adapters.ProductCategoryAdapter
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.databinding.FragmentProductCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductCategoryFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductCategoryBinding
    val productMutableList: MutableList<ApiProductsModel> = mutableListOf()
    val bundle: ProductCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProductCategoryBinding.inflate(inflater, container, false)

        productLoadData()
        viewBinding.fragmentProductCategoryAllRecyclerView.adapter =
            ProductCategoryAdapter(
                requireContext(),
                productMutableList
            )
        viewBinding.fragmentProductCategoryAllRecyclerView.layoutManager =
            GridLayoutManager(context, 2)
        viewBinding.fragmentProductCategoryAllToolbar.title = bundle.categoryTitle

        if (productMutableList.size != 0) {
            viewBinding.fragmentProductCategoryAllPgb.visibility = View.GONE
        }
        viewBinding.fragmentProductCategoryAllToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
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
                    viewBinding.fragmentProductCategoryAllPgb.visibility = View.GONE
                    if (productMutableList.size == 0) {
                        viewBinding.fragmentProductCategoryAllRecyclerView.adapter =
                            ProductCategoryAdapter(
                                context!!,
                                productMutableList
                            )
                        viewBinding.fragmentProductCategoryAllRecyclerView.layoutManager =
                            GridLayoutManager(context, 2)
                    }
                    //The listing changes depending on which category is clicked
                    var indeks = 0
                    while (indeks < response.body()!!.size) {
                        if (bundle.category == response.body()!!.get(indeks).category) {
                            productMutableList.add(
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
                }
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}