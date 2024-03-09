package com.example.alsess.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductDetailBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.viewmodel.ProductDetailViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val bundle: ProductDetailFragmentArgs by navArgs()
    private val productArrayList = ArrayList<ApiProductsModel>()
    private lateinit var productDetailViewModel: ProductDetailViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentProductDetailBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        productDetailViewModel = ViewModelProvider(this)[ProductDetailViewModel::class.java]
        productDetailViewModel.loadProductData(bundle.id)
        viewModelObserve()

        viewBinding.fragmentProductDetailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
    }

    private fun viewModelObserve() {
        val currentUser = firebaseAuth.currentUser
        productDetailViewModel.productMLD.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                productArrayList.addAll(it)
                exportInformationToViews()
                productDetailViewModel.controlBasketData(requireContext(), bundle.id)
                productDetailViewModel.controlFavoritesData(requireContext(), bundle.id)
                addDataFavorites()
                addDataBasket()
            }
        })

        productDetailViewModel.loadMLD.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                if (it) {
                    viewBinding.fragmentProductDetailPgb.visibility = View.GONE
                    viewBinding.fragmentProductDetailCardView.visibility = View.VISIBLE
                    viewBinding.fragmentProductDetailCl.visibility = View.VISIBLE
                }
            }
        })

        productDetailViewModel.controlFavorites.observe(viewLifecycleOwner, Observer { control ->
            if (currentUser != null) {
                viewBinding.fragmentProductDetailTgbAddFavorites.isChecked = !control
            } else {
                viewBinding.fragmentProductDetailTgbAddFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
                viewBinding.fragmentProductDetailTgbAddFavorites.setOnClickListener {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

        })

        productDetailViewModel.controlBasket.observe(viewLifecycleOwner, Observer { control ->
            viewBinding.fragmentProductDetailTgbAddBasket.isChecked = !control
        })
    }

    private fun exportInformationToViews() {
        viewBinding.fragmentProductDetailTxvProductName.text =
            productArrayList[0].title

        Glide.with(requireContext()).load(productArrayList[0].image)
            .into(viewBinding.fragmentProductDetailImvProduct)

        viewBinding.fragmentProductDetailTxvTotalPrice.text =
            productArrayList[0].price.toString()

        viewBinding.fragmentProductDetailTxvDescription.text =
            productArrayList[0].description

        viewBinding.fragmentProductDetailRtb.rating =
            productArrayList[0].rating.rate.toFloat()

        viewBinding.fragmentProductDetailTxvRating.text =
            productArrayList[0].rating.rate.toString()

        viewBinding.fragmentProductDetailTxvCategory.text =
            productArrayList[0].category.replaceFirstChar {
                it.uppercase()
            }
    }

    private fun addDataFavorites() {
        viewBinding.fragmentProductDetailTgbAddFavorites.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                productDetailViewModel.addFavoritesData(requireContext())
            } else {
                productDetailViewModel.deleteFavorites(requireContext(), bundle.id)
            }
        }

    }

    private fun addDataBasket() {
        viewBinding.fragmentProductDetailTgbAddBasket.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                productDetailViewModel.addBasketData(requireContext())
            } else {
                productDetailViewModel.deleteBasket(requireContext(), bundle.id)
            }
        }
    }
}
