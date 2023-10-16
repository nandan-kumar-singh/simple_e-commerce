package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.adapters.ProductsRecyclerViewAdapter
import com.example.alsess.apimodels.ApiProductsModel
import com.example.alsess.apis.ProductsAPI
import com.example.alsess.databinding.FragmentProductsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsFragment : Fragment() {
    private lateinit var viewBinding : FragmentProductsBinding
    val idList = ArrayList<Long>()
    val titleList = ArrayList<String>()
    val priceList = ArrayList<Double>()
    val imageList = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewBinding= FragmentProductsBinding.inflate(inflater, container, false)
        loadProducts()
        // fragmente geri dönünce progresbarın çalışmaasını engeller
        if(idList.size >0){
            viewBinding.progressBar.visibility = View.GONE
        }

        viewBinding.recylerView.adapter = context?.let { ProductsRecyclerViewAdapter(it, idList, titleList, priceList, imageList) }
        viewBinding.recylerView.layoutManager = GridLayoutManager(context,2)

        return viewBinding.root
    }

    //Gelen verileri yükler ve bunları listeleme görünümüne atar
    fun loadProducts(){
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiLinks.PRODUCTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ProductsAPI:: class.java)
        val call = service.loadData()
        call.enqueue(object : Callback<List<ApiProductsModel>>{
            override fun onResponse(call: Call<List<ApiProductsModel>>, response: Response<List<ApiProductsModel>>, ) {
                // ilk eklemeden sonra tekrar tekrar adapteri çalıştırmak yerine, yerini oncreateviewde çalışan kısma bırakır
                if(idList.size == 0){
                    viewBinding.recylerView.adapter = context?.let { ProductsRecyclerViewAdapter(it, idList, titleList, priceList, imageList) }
                    viewBinding.recylerView.layoutManager = GridLayoutManager(context,2)
                }

                if (response.isSuccessful) {
                    viewBinding.progressBar.visibility = View.GONE
                    //veriler döngü sayesinde kaydedilir
                    //if ile veri listesinin birden fazla kaydı engellenir
                    if(idList.size < response.body()!!.size){
                        var indeks = 0
                        while (indeks < response.body()!!.size) {
                            idList.add(response.body()!!.get(indeks).id)
                            titleList.add(response.body()!!.get(indeks).title)
                            priceList.add(response.body()!!.get(indeks).price)
                            imageList.add(response.body()!!.get(indeks).image)
                            indeks++

                        }

                    }

                }
            }
            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}