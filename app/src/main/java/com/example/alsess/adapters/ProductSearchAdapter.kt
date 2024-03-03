package com.example.alsess.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductSearchRowBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.BasketSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.BasketSQLiteDataHelper
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.view.LoginActivity
import com.example.alsess.view.ProductSearchFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class ProductSearchAdapter(
    val context : Context,
    var productArrayList: ArrayList<ApiProductsModel>
) : RecyclerView.Adapter<ProductSearchAdapter.ProductSearchVH>() {
    private lateinit var firebaseAuth: FirebaseAuth
    class ProductSearchVH(val viewBinding: FragmentProductSearchRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchVH {
        val view = FragmentProductSearchRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductSearchVH(view)
    }

    override fun getItemCount(): Int {
        return productArrayList.size
    }

    override fun onBindViewHolder(holder: ProductSearchVH, position: Int) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val arrayPosition = productArrayList.get(position)

        holder.viewBinding.rowProductSearchTxvName.text = arrayPosition.title

        Glide.with(context).load(arrayPosition.image)
            .into(holder.viewBinding.rowProductSearchImvProduct)

        holder.viewBinding.rowProductSearchTxvPrice.text =
            "${arrayPosition.price.toString()}$"

        holder.viewBinding.rowProductSearchRtbStar.rating =
            arrayPosition.rating.rate.toFloat()

        holder.viewBinding.rowProductSearchTxvRating.text =
            arrayPosition.rating.rate.toString()

        //Data cannot be added to the favorites list if no user is logged in
        if (currentUser != null) {
            addDataFavorites(holder.viewBinding.rowProductSearchTgbFavorites, position)
        } else {
            holder.viewBinding.rowProductSearchTgbFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
            holder.viewBinding.rowProductSearchTgbFavorites.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }

        }


        addDataBasket(holder.viewBinding.rowProductSearchTgbAddBasket, position)

        holder.viewBinding.rowProductSearchCardView.setOnClickListener {
            val dataDrections =
                ProductSearchFragmentDirections.actionProductSearchFragmentToProductDetailFragment(
                    arrayPosition.id
                )
            Navigation.findNavController(it).navigate(dataDrections)
        }

    }
    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()
        val arrayPositionFav = productArrayList.get(position)

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        arrayPositionFav.id
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        arrayPositionFav.id,
                        arrayPositionFav.title,
                        arrayPositionFav.price,
                        arrayPositionFav.image
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, arrayPositionFav.id)
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(favoritesDataHelper, arrayPositionFav.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }

    fun addDataBasket(toggleButton: ToggleButton, position: Int) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketDAO = BasketSQLiteDao()
        val arrayPositionBasket = productArrayList.get(position)
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        arrayPositionBasket.id
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper,
                        arrayPositionBasket.id,
                        arrayPositionBasket.title,
                        arrayPositionBasket.price,
                        arrayPositionBasket.image,
                        1
                    )
                }
            } else {
                basketDAO.deleteProduts(basketDataHelper, arrayPositionBasket.id)
            }
        }
        //Toggle button is checked as true if added to basket, false if not
        if (basketDAO.controlBasket(basketDataHelper, arrayPositionBasket.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }

    }
}
