package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.adapters.ProductCategoryAdapter
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.databinding.FragmentProductCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductCategoryFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductCategoryBinding
    val productIdList = ArrayList<Long>()
    val productNameList = ArrayList<String>()
    val productPriceList = ArrayList<Double>()
    val productImageList = ArrayList<String>()
    val productRatingList = ArrayList<Double>()
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
                productIdList,
                productNameList,
                productPriceList,
                productImageList,
                productRatingList
            )
        viewBinding.fragmentProductCategoryAllRecyclerView.layoutManager =
            GridLayoutManager(context, 2)
        viewBinding.fragmentProductCategoryAllToolbar.title = bundle.categoryTitle

        if (productIdList.size != 0) {
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
                    if (productIdList.size == 0) {
                        viewBinding.fragmentProductCategoryAllRecyclerView.adapter =
                            ProductCategoryAdapter(
                                context!!,
                                productIdList,
                                productNameList,
                                productPriceList,
                                productImageList,
                                productRatingList
                            )
                        viewBinding.fragmentProductCategoryAllRecyclerView.layoutManager =
                            GridLayoutManager(context, 2)
                    }
                    //The listing changes depending on which category is clicked
                    var indeks = 0
                    while (indeks < response.body()!!.size) {
                        if (bundle.category == response.body()!!.get(indeks).category) {
                            productIdList.add(response.body()!!.get(indeks).id)
                            productNameList.add(
                                response.body()!!.get(indeks).title.replace(
                                    "'",
                                    " "
                                )
                            )
                            productPriceList.add(response.body()!!.get(indeks).price)
                            productImageList.add(response.body()!!.get(indeks).image)
                            productRatingList.add(response.body()!!.get(indeks).rating.rate)
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