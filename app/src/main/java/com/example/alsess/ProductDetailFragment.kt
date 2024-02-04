package com.example.alsess

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.databinding.FragmentProductDetailBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductDetailBinding
    val bundle: ProductDetailFragmentArgs by navArgs()
    val productIdList = ArrayList<Long>()
    val productNameList = ArrayList<String>()
    val productPriceList = ArrayList<Double>()
    val productImageList = ArrayList<String>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        loadDetailDaata()
        addDataFavoritesAndControl()
        addDataBasketAndControl()

        viewBinding.fragmentProductDetailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
    }

    fun loadDetailDaata() {
        val retrofit = ProductsRetrofit()
        retrofit.service.loadData()
            .enqueue(object : Callback<List<ApiProductsModel>> {
                @SuppressLint("CheckResult")
                override fun onResponse(
                    call: Call<List<ApiProductsModel>>,
                    response: Response<List<ApiProductsModel>>,
                ) {
                    if (response.isSuccessful) {
                        var indeks = 0
                        viewBinding.fragmentProductDetailPgb.visibility = View.GONE
                        viewBinding.fragmentProductDetailCl.visibility = View.VISIBLE
                        viewBinding.fragmentProductDetailCardView.visibility = View.VISIBLE
                        while (indeks < response.body()!!.size) {
                            if (response.body()!!.get(indeks).id == bundle.id.toLong()) {
                                Glide.with(context!!).load(response.body()!!.get(indeks).image)
                                    .into(viewBinding.fragmentProductDetailImvProduct)
                                viewBinding.fragmentProductDetailTxvProductName.text =
                                    response.body()!!.get(indeks).title
                                viewBinding.fragmentProductDetailTxvTotalPrice.text =
                                    response.body()!!.get(indeks).price.toString()
                                viewBinding.fragmentProductDetailTxvDescription.text =
                                    response.body()!!.get(indeks).description
                                viewBinding.fragmentProductDetailRtb.rating =
                                    response.body()!!.get(indeks).rating.rate.toFloat()
                                viewBinding.fragmentProductDetailTxvRating.text =
                                    response.body()!!.get(indeks).rating.rate.toString()
                                viewBinding.fragmentProductDetailTxvCategory.text =
                                    response.body()!!.get(indeks).category.replaceFirstChar {
                                        it.uppercase()
                                    }
                                productIdList.add(response.body()!!.get(indeks).id)
                                productNameList.add(response.body()!!.get(indeks).title)
                                productPriceList.add(response.body()!!.get(indeks).price)
                                productImageList.add(response.body()!!.get(indeks).image)

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

    fun addDataFavoritesAndControl() {
        val favoritesDataHelper = FavoritesSqliteDataHelper(requireContext())
        val favoritesDAO = FavoritesSqliteDao()

        viewBinding.fragmentProductDetailTgbAddFavorites.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        bundle.id.toLong()
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper, productIdList.get(0), productNameList.get(0),
                        productPriceList.get(0), productImageList.get(0)
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, bundle.id.toLong())
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(favoritesDataHelper, bundle.id.toLong()) == 0) {
            viewBinding.fragmentProductDetailTgbAddFavorites.isChecked = false
        } else {
            viewBinding.fragmentProductDetailTgbAddFavorites.isChecked = true
        }
    }

    fun addDataBasketAndControl() {
        val basketDataHelper = BasketSqliteDataHelper(requireContext())
        val basketDAO = BasketSqliteDao()

        viewBinding.fragmentProductDetailTgbAddBasket.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        bundle.id.toLong()
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper, productIdList.get(0), productNameList.get(0),
                        productPriceList.get(0), productImageList.get(0)
                    )
                }
            } else {
                basketDAO.deleteProduts(basketDataHelper, bundle.id.toLong())
            }
        }
        //Toggle button is checked as true if added to basket, false if not
        if (basketDAO.controlBasket(basketDataHelper, bundle.id.toLong()) == 0) {
            viewBinding.fragmentProductDetailTgbAddBasket.isChecked = false
        } else {
            viewBinding.fragmentProductDetailTgbAddBasket.isChecked = true
        }
    }
}
