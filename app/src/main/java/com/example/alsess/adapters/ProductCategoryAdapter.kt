package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.databinding.FragmentProductCategoryRowBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import com.example.alsess.view.ProductCategoryFragmentDirections

class ProductCategoryAdapter(
    val context: Context,
    val productMutableList: MutableList<ApiProductsModel>
) : RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryAllVH>() {
    class ProductCategoryAllVH(val viewBinding: FragmentProductCategoryRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryAllVH {
        val view = FragmentProductCategoryRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductCategoryAllVH(view)
    }

    override fun getItemCount(): Int {
        return productMutableList.size

    }

    override fun onBindViewHolder(holder: ProductCategoryAllVH, position: Int) {
        val mutablePosition = productMutableList.get(position)
        holder.viewBinding.rowProductCategoryAllTxvName.text = mutablePosition.title
        Glide.with(context).load(mutablePosition.image)
            .into(holder.viewBinding.rowProductCategoryAllImvProduct)
        holder.viewBinding.rowProductCategoryAllTxvPrice.text = "${mutablePosition.price.toString()}$"
        holder.viewBinding.rowProductCategoryAllRtbStar.rating =
            mutablePosition.rating.rate.toFloat()
        holder.viewBinding.rowProductCategoryAllTxvRating.text =
            mutablePosition.rating.rate.toString()
        addDataFavorites(holder.viewBinding.rowProductCategoryAllTgbFavorites, position)
        addDataBasket(holder.viewBinding.rowProductCategoryAllTgbAddBasket, position)
        holder.viewBinding.rowProductCategoryAllCardView.setOnClickListener {
            val dataDrections =
                ProductCategoryFragmentDirections.actionProductCategoryFragmentToProductDetailFragment(
                    mutablePosition.id
                )
            Navigation.findNavController(it).navigate(dataDrections)
        }
    }

    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val favoritesDAO = FavoritesSqliteDao()
        val mutablePositionFav = productMutableList.get(position)

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        mutablePositionFav.id
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        mutablePositionFav.id,
                        mutablePositionFav.title,
                        mutablePositionFav.price,
                        mutablePositionFav.image
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, mutablePositionFav.id)
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(favoritesDataHelper, mutablePositionFav.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }

    fun addDataBasket(toggleButton: ToggleButton, position: Int) {
        val basketDataHelper = BasketSqliteDataHelper(context)
        val basketDAO = BasketSqliteDao()
        val mutablePositionBasket = productMutableList.get(position)
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        mutablePositionBasket.id
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper,
                        mutablePositionBasket.id,
                        mutablePositionBasket.title,
                        mutablePositionBasket.price,
                        mutablePositionBasket.image,
                        1
                    )
                }
            } else {
                basketDAO.deleteProduts(basketDataHelper, mutablePositionBasket.id)
            }
        }
        //Toggle button is checked as true if added to basket, false if not
        if (basketDAO.controlBasket(basketDataHelper, mutablePositionBasket.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }

    }
}