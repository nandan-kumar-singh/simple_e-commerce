package com.example.alsess.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alsess.service.ProductsRetrofitService
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductDetailBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.BasketSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.BasketSQLiteDataHelper
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductDetailBinding
    val bundle: ProductDetailFragmentArgs by navArgs()
    val productMutableList: MutableList<ApiProductsModel> = mutableListOf()
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentProductDetailBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        loadDetailDaata()

        //Data cannot be added to the favorites list if no user is logged in
        if (currentUser != null) {
            addDataFavoritesAndControl()
        } else {
            viewBinding.fragmentProductDetailTgbAddFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
            viewBinding.fragmentProductDetailTgbAddFavorites.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        addDataBasketAndControl()

        viewBinding.fragmentProductDetailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
    }

    fun loadDetailDaata() {
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData()
            .enqueue(object : Callback<List<ApiProductsModel>> {
                @SuppressLint("CheckResult")
                override fun onResponse(
                    call: Call<List<ApiProductsModel>>,
                    response: Response<List<ApiProductsModel>>,
                ) {
                    if (response.isSuccessful) {
                        viewBinding.fragmentProductDetailPgb.visibility = View.GONE
                        viewBinding.fragmentProductDetailCl.visibility = View.VISIBLE
                        viewBinding.fragmentProductDetailCardView.visibility = View.VISIBLE

                        var indeks = 0
                        while (indeks < response.body()!!.size) {
                            if (response.body()!!.get(indeks).id == bundle.id.toLong()) {
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
                                exportInformationToViews()
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

    fun exportInformationToViews() {
        viewBinding.fragmentProductDetailTxvProductName.text =
            productMutableList.get(0).title

        Glide.with(context!!).load(productMutableList.get(0).image)
            .into(viewBinding.fragmentProductDetailImvProduct)

        viewBinding.fragmentProductDetailTxvTotalPrice.text =
            productMutableList.get(0).price.toString()

        viewBinding.fragmentProductDetailTxvDescription.text =
            productMutableList.get(0).description

        viewBinding.fragmentProductDetailRtb.rating =
            productMutableList.get(0).rating.rate.toFloat()

        viewBinding.fragmentProductDetailTxvRating.text =
            productMutableList.get(0).rating.rate.toString()

        viewBinding.fragmentProductDetailTxvCategory.text =
            productMutableList.get(0).category.replaceFirstChar {
                it.uppercase()
            }
    }

    fun addDataFavoritesAndControl() {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(requireContext())
        val favoritesDAO = FavoritesSQLiteDao()

        viewBinding.fragmentProductDetailTgbAddFavorites.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        bundle.id.toLong()
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        productMutableList.get(0).id,
                        productMutableList.get(0).title,
                        productMutableList.get(0).price,
                        productMutableList.get(0).image
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
        val basketDataHelper = BasketSQLiteDataHelper(requireContext())
        val basketDAO = BasketSQLiteDao()

        viewBinding.fragmentProductDetailTgbAddBasket.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        bundle.id.toLong()
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper, productMutableList.get(0).id,
                        productMutableList.get(0).title,
                        productMutableList.get(0).price,
                        productMutableList.get(0).image
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
