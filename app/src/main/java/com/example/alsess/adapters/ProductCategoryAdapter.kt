package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.ProductCategoryFragmentDirections
import com.example.alsess.ProductsFragmentDirections
import com.example.alsess.databinding.FragmentProductCategoryRowBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper

class ProductCategoryAdapter(
    val context: Context,
    val idList: ArrayList<Long>,
    val titleList: ArrayList<String>,
    val priceList: ArrayList<Double>,
    val imageList: ArrayList<String>,
    val ratingList: ArrayList<Double>
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
        return idList.size

    }

    override fun onBindViewHolder(holder: ProductCategoryAllVH, position: Int) {
        holder.viewBinding.rowProductCategoryAllTxvName.text = titleList.get(position)
        Glide.with(context).load(imageList.get(position))
            .into(holder.viewBinding.rowProductCategoryAllImvProduct)
        holder.viewBinding.rowProductCategoryAllTxvPrice.text = priceList.get(position).toString()
        holder.viewBinding.rowProductCategoryAllRtbStar.rating = ratingList.get(position).toFloat()
        holder.viewBinding.rowProductCategoryAllTxvRating.text = ratingList.get(position).toString()
        addDataFavorites(holder.viewBinding.rowProductCategoryAllTgbFavorites, position)
        addDataBasket(holder.viewBinding.rowProductCategoryAllTgbAddBasket, position)
        holder.viewBinding.rowProductCategoryAllCardView.setOnClickListener {
            val dataDrections =
                ProductCategoryFragmentDirections.actionProductCategoryFragmentToProductDetailFragment(
                    idList.get(position).toInt()
                )
            Navigation.findNavController(it).navigate(dataDrections)
        }
    }

    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val favoritesDAO = FavoritesSqliteDao()

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        idList.get(position)
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        idList.get(position),
                        titleList.get(position).replace("'", " "),
                        priceList.get(position).toString(),
                        imageList.get(position)
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, idList.get(position))
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(favoritesDataHelper, idList.get(position)) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }

    fun addDataBasket(toggleButton: ToggleButton, position: Int) {
        val basketDataHelper = BasketSqliteDataHelper(context)
        val basketDAO = BasketSqliteDao()

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        idList.get(position)
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper,
                        idList.get(position),
                        titleList.get(position).replace("'", " "),
                        priceList.get(position),
                        imageList.get(position),
                        1
                    )
                }
            } else {
                basketDAO.deleteProduts(basketDataHelper, idList.get(position))
            }
        }
        //Toggle button is checked as true if added to basket, false if not
        if (basketDAO.controlBasket(basketDataHelper, idList.get(position)) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }

    }
}