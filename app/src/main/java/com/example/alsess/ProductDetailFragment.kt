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
import com.example.alsess.apis.ProductsAPI
import com.example.alsess.databinding.FragmentProductDetailBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductDetailBinding
    val bundle: ProductDetailFragmentArgs by navArgs()
    val idList = ArrayList<Long>()
    val titleList = ArrayList<String>()
    val priceList = ArrayList<Int>()
    val imageList = ArrayList<String>()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        viewBinding = FragmentProductDetailBinding.inflate(inflater, container, false)
        loadDetail()
        viewBinding.relative.visibility = View.GONE

        //detaytaki geri tuşu ile detaya hangi fragmentten girildiyse oraya geri döndürülür
        viewBinding.backButton.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
        }

        fun loadDetail() {
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiLinks.PRODUCTS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ProductsAPI::class.java)
            val call = service.loadData()
            call.enqueue(object : Callback<List<ApiProductsModel>> {
                override fun onResponse(
                    call: Call<List<ApiProductsModel>>,
                    response: Response<List<ApiProductsModel>>,
                ) {
                    if (response.isSuccessful) {
                        //Ürünlr sayfasından gelen id bilgilerine göre ürün detayı görüntülenir
                        viewBinding.relative.visibility = View.VISIBLE
                        viewBinding.progressBar.visibility = View.GONE
                        val indeks = bundle.id - 1
                        idList.add(response.body()!!.get(indeks).id)
                        titleList.add(response.body()!!.get(indeks).title)
                        priceList.add(response.body()!!.get(indeks).price.toInt())
                        imageList.add(response.body()!!.get(indeks).image)
                        val basketDataHelper = BasketSqliteDataHelper(context!!)
                        val favoritesDataHelper = FavoritesSqliteDataHelper(context!!)



                        //FAVORİTES PROCESS
                        //Favoriler butonu ile ekleme kontrolü yapıldıktan sonra eklenir butonu devre dışı bırakınca veri silinir
                        viewBinding.favoritesButton2.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                FavoritesSqliteDao().deleteFavorites(favoritesDataHelper, idList.get(0))
                                viewBinding.favoritesButton2.setBackgroundResource(R.drawable.asset_favorites_white)
                            } else {
                                if (FavoritesSqliteDao().controlFavorites(favoritesDataHelper, idList.get(0)) == 0) {
                                    FavoritesSqliteDao().addFavorites(
                                        favoritesDataHelper,
                                        idList.get(0),
                                        titleList.get(0),
                                        priceList.get(0).toString(),
                                        imageList.get(0)
                                    )
                                    viewBinding.favoritesButton2.setBackgroundResource(R.drawable.asset_favorites_red)
                                }
                            }
                        }

                        if (FavoritesSqliteDao().controlFavorites(favoritesDataHelper, idList.get(0)) == 0) {
                            viewBinding.favoritesButton2.setBackgroundResource(R.drawable.asset_favorites_white)
                        } else {
                            viewBinding.favoritesButton2.isChecked = false
                            viewBinding.favoritesButton2.setBackgroundResource(R.drawable.asset_favorites_red)
                        }


                        //BASKET PROCESS
                        //Satın al butonu ile ekleme kontrolü yapıldıktan sonra eklenir butonu devre dışı bırakınca veri silinir
                        viewBinding.addBasket.setOnClickListener {
                            if (BasketSqliteDao().controlBasket(
                                    basketDataHelper,
                                    idList.get(0)
                                ) == 0
                            ) {
                                BasketSqliteDao().addBasket(
                                    basketDataHelper,
                                    idList.get(0),
                                    titleList.get(0),
                                    priceList.get(0).toDouble(),
                                    imageList.get(0),
                                    1
                                )
                                viewBinding.addBasket.setBackgroundResource(R.drawable.acceptance_button_shape)
                            } else {
                                BasketSqliteDao().deleteProduts(basketDataHelper, idList.get(0))
                                viewBinding.addBasket.setBackgroundResource(R.drawable.buy_button_shape)
                            }

                        }

                        //favori butonunun renk ve ekleme kontrolü yapılır
                        if(BasketSqliteDao().controlBasket(basketDataHelper,idList.get(0)) == 0){
                            viewBinding.addBasket.setBackgroundResource(R.drawable.buy_button_shape)
                        }else{
                            viewBinding.addBasket.setBackgroundResource(R.drawable.acceptance_button_shape)
                        }

                        // gelen id bilgilerine göre ürün bilgileri görünümlere tanımlanır
                        context?.let {
                            Glide.with(it).load(response.body()!!.get(indeks).image).into(viewBinding.imageView)
                            viewBinding.title.text = response.body()!!.get(indeks).title
                            viewBinding.ratingBar.rating = response.body()!!.get(indeks).rating.rate.toFloat()
                            viewBinding.rating.text = response.body()!!.get(indeks).rating.rate.toString()
                            viewBinding.category.text = response.body()!!.get(indeks).category
                            viewBinding.description.text = response.body()!!.get(indeks).description
                            viewBinding.totalPrice.text = response.body()!!.get(indeks).price.toString() + "$"
                        }
                    }

                }

                override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }

    }
