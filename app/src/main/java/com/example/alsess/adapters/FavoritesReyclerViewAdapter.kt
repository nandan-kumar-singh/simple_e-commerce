package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.databinding.FragmentFavoritesRowBinding
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import com.example.alsess.view.FavoritesFragmentDirections


class FavoritesReyclerViewAdapter(val context: Context) : RecyclerView.Adapter<FavoritesReyclerViewAdapter.FavoritesVH>() {
    class FavoritesVH(val viewBinding : FragmentFavoritesRowBinding) : RecyclerView.ViewHolder(viewBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesVH {
        val view = FragmentFavoritesRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoritesVH(view)
    }

    override fun getItemCount(): Int {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val titlelist = FavoritesSqliteDao().readFavorites(favoritesDataHelper)
        return titlelist.size
    }

    override fun onBindViewHolder(holder: FavoritesVH, position: Int) {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val favoritesList = FavoritesSqliteDao().readFavorites(favoritesDataHelper)
        holder.viewBinding.recyclerRowFavoritesTxvProductName.text = favoritesList.get(position).title.replace("'"," ")
        holder.viewBinding.recyclerRowFavoritesTxvPrice.text = favoritesList.get(position).price.toString()
        Glide.with(context!!).load(favoritesList.get(position).image_url).into(holder.viewBinding.recyclerRowFavoritesImvProduct)

        //Products in favorites are displayed on the detail page
        holder.viewBinding.recyclerRowFavoritesCardViewProduct.setOnClickListener {
            val navArgs = FavoritesFragmentDirections.toProductsDetail(favoritesList.get(position).id.toInt())
            Navigation.findNavController(it).navigate(navArgs)
        }

        holder.viewBinding.recyclerRowFavoritesBtnFavorites.setOnClickListener {
            FavoritesSqliteDao().deleteFavorites(favoritesDataHelper,favoritesList.get(position).id)
            notifyDataSetChanged()
        }
    }
}