package com.example.aaaaaa

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.databinding.FragmentProductChildRowBinding
import com.example.alsess.recyclerviewmodel.ProductChildModel
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import com.example.alsess.view.ProductChildFragmentDirections

class ProductChildAdapter(val context: Context, val childList: List<ProductChildModel>) :
    RecyclerView.Adapter<ProductChildAdapter.ProductChildVH>() {
    class ProductChildVH(val viewBinding: FragmentProductChildRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductChildVH {
        val view = FragmentProductChildRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductChildVH(view)
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductChildVH, position: Int) {
        holder.viewBinding.rowProductChildTxvName.text = childList.get(position).productName

        Glide.with(context).load(childList.get(position).productImage)
            .into(holder.viewBinding.rowProductChildImvProduct)

        holder.viewBinding.rowProductChildTxvPrice.text =
            "${childList.get(position).productPrice.toString()}$"


        holder.viewBinding.rowProductChildRtbStar.rating =
            childList.get(position).productRating.toFloat()

        holder.viewBinding.rowProductChildTxvRating.text =
            childList.get(position).productRating.toString()

        addDataFavorites(holder.viewBinding.rowProductChildTgbAddFavorites, position)

        //Gets the id of the clicked product and shows the product in the product detail
        holder.viewBinding.rowProductChildCardView.setOnClickListener {
            val navARg = ProductChildFragmentDirections.toProductDetail(
                childList.get(position).productId
            )
            Navigation.findNavController(it).navigate(navARg)
        }


    }

    //To add to favorites from child in nested recyclerview
    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val favoritesDAO = FavoritesSqliteDao()

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        childList.get(position).productId
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        childList.get(position).productId,
                        childList.get(position).productName,
                        childList.get(position).productPrice,
                        childList.get(position).productImage
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, childList.get(position).productId)
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(
                favoritesDataHelper,
                childList.get(position).productId
            ) == 0
        ) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }
}