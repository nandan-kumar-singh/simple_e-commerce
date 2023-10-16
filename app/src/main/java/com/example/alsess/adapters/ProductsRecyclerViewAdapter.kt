package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.ProductsFragmentDirections
import com.example.alsess.R
import com.example.alsess.databinding.HomeFragmentRowBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper


class ProductsRecyclerViewAdapter(val context : Context,
                                  val idList : ArrayList<Long>,
                                  val titleList : ArrayList<String>,
                                  val priceList : ArrayList<Double>,
                                  val imageList : ArrayList<String>) : RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductsVH>() {
    class ProductsVH(var viewBinding: HomeFragmentRowBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        val view = HomeFragmentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsVH(view)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }
    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val basketDataHelper = BasketSqliteDataHelper(context)
        //Gelen ürün bilgilerini görünümlere aktarıp listeleme sağlanır
        holder.viewBinding.titleText.text = titleList.get(position)
        holder.viewBinding.priceText.text = priceList.get(position).toString() + " $"
        Glide.with(context).load(imageList.get(position)).into(holder.viewBinding.imageView)

        //Ürün detayını görüntülemek için kullanılır
        //Ürün bilgileri id yardımıyla görüntülenir
        holder.viewBinding.productsCardView.setOnClickListener {
            val dataArg = ProductsFragmentDirections.toProductsDetail(idList.get(position).toInt())
            Navigation.findNavController(it).navigate(dataArg)
        }

        //Satın al butonuna basıldığında ürünler sepet sayfasına veritabanı sayesinde aktarılır
        //Buton iptal edilince veriler veritabanından silinir
        holder.viewBinding.buttonAddBasket.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                BasketSqliteDao().deleteProduts(basketDataHelper,idList.get(position))
                holder.viewBinding.buttonAddBasket.setBackgroundResource(R.drawable.buy_button_shape)
            }else{
                if(BasketSqliteDao().controlBasket(basketDataHelper,idList.get(position).toLong()) == 0){
                    BasketSqliteDao().addBasket(basketDataHelper,idList.get(position),titleList.get(position).replace("'"," "),priceList.get(position),imageList.get(position),1)
                    holder.viewBinding.buttonAddBasket.setBackgroundResource(R.drawable.acceptance_button_shape)
                }
            }
        }

        // veri eklendikten sonra sepete ekleme butonunun konumun kontrolü yapılır
        if(BasketSqliteDao().controlBasket(basketDataHelper,idList.get(position)) > 0){
            holder.viewBinding.buttonAddBasket.isChecked = false
            if(holder.viewBinding.buttonAddBasket.isChecked == false){
                holder.viewBinding.buttonAddBasket.setBackgroundResource(R.drawable.acceptance_button_shape)
            }
        }else{
            holder.viewBinding.buttonAddBasket.isChecked = true
            if(holder.viewBinding.buttonAddBasket.isChecked == true){
                holder.viewBinding.buttonAddBasket.setBackgroundResource(R.drawable.buy_button_shape)
            }
        }

        //favori ekleme butonuna basıldığında ürünler sepet sayfasına veritabanı sayesinde aktarılır
        //Buton iptal edilince veriler veritabanından silinir
        holder.viewBinding.favoritesButton.setOnCheckedChangeListener { buttonView, isChecked ->
           if(isChecked){
               FavoritesSqliteDao().deleteFavorites(favoritesDataHelper,idList.get(position).toLong()  )
               holder.viewBinding.favoritesButton.setBackgroundResource(R.drawable.asset_favorites_white)
           }else{
               if(FavoritesSqliteDao().controlFavorites(favoritesDataHelper,idList.get(position)) == 0){
                   FavoritesSqliteDao().addFavorites(favoritesDataHelper,idList.get(position),titleList.get(position).replace("'"," "),priceList.get(position).toString() ,imageList.get(position))
                   holder.viewBinding.favoritesButton.setBackgroundResource(R.drawable.asset_favorites_red)
               }
           }
       }

        // veri eklendikten sonra favorilere ekleme butonunun konumun kontrolü yapılır
        if(FavoritesSqliteDao().controlFavorites(favoritesDataHelper,idList.get(position)) > 0){
            holder.viewBinding.favoritesButton.isChecked = false
            if(holder.viewBinding.favoritesButton.isChecked == false){
                holder.viewBinding.favoritesButton.setBackgroundResource(R.drawable.asset_favorites_red)
            }
        }else{
            holder.viewBinding.favoritesButton.isChecked = true
            if(holder.viewBinding.favoritesButton.isChecked == true){
                holder.viewBinding.favoritesButton.setBackgroundResource(R.drawable.asset_favorites_white)
            }
        }

    }
}