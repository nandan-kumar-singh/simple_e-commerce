package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.databinding.FragmentFavoritesRowBinding
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.view.FavoritesFragmentDirections
import java.util.*


class FavoritesAdapter(val context: Context) : RecyclerView.Adapter<FavoritesAdapter.FavoritesVH>() {
    class FavoritesVH(val viewBinding : FragmentFavoritesRowBinding) : RecyclerView.ViewHolder(viewBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesVH {
        val view = FragmentFavoritesRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoritesVH(view)
    }

    override fun getItemCount(): Int {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val titlelist = FavoritesSQLiteDao().readFavorites(favoritesDataHelper)
        return titlelist.size
    }

    override fun onBindViewHolder(holder: FavoritesVH, position: Int) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesList = FavoritesSQLiteDao().readFavorites(favoritesDataHelper)
        Collections.reverse(favoritesList)
        holder.viewBinding.recyclerRowFavoritesTxvProductName.text = favoritesList.get(position).title.replace("'"," ")
        holder.viewBinding.recyclerRowFavoritesTxvPrice.text = favoritesList.get(position).price.toString()
        Glide.with(context).load(favoritesList.get(position).image_url).into(holder.viewBinding.recyclerRowFavoritesImvProduct)

        //Products in favorites are displayed on the detail page
        holder.viewBinding.recyclerRowFavoritesCardViewProduct.setOnClickListener {
            val navArgs = FavoritesFragmentDirections.actionFavoritesFragmentToProductDetailFragment(favoritesList.get(position).id)
            Navigation.findNavController(it).navigate(navArgs)
        }

        holder.viewBinding.recyclerRowFavoritesBtnFavorites.setOnClickListener {
            FavoritesSQLiteDao().deleteFavorites(favoritesDataHelper,favoritesList.get(position).id)
            notifyDataSetChanged()
        }
    }
}