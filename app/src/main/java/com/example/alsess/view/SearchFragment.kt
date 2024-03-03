package com.example.alsess.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.service.ProductsRetrofitService
import com.example.alsess.adapters.SearchAdapter
import com.example.alsess.databinding.FragmentSearchBinding
import com.example.alsess.model.ApiProductsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: SearchAdapter
    private lateinit var viewBinding: FragmentSearchBinding
    val searchItemList = HashSet<String>()

    override fun onStart() {
        super.onStart()
        val sharedPreferences =
            context?.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()
        sharedPreferencesEditor?.clear()
        sharedPreferencesEditor?.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        viewBinding.fragmentSearchToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        recyclerView = viewBinding.fragmentSearchRecyclerView
        searchView = viewBinding.fragmentSearchSchv
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        searchItemLoadData()
        adapter = SearchAdapter(searchItemList.toList())
        recyclerView.adapter = adapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //The entered data is confirmed and sent to the product search page
                if (query != null) {
                    if (query.length >= 2) {
                        val dataDirections =
                            SearchFragmentDirections.actionSearchFragmentToProductSearchFragment(
                                query
                            )
                        view?.let {
                            Navigation.findNavController(it).navigate(dataDirections)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


        return viewBinding.root
    }

    private fun filterList(query: String?) {
        if (query != null) {
            if (query.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                val filteredList = ArrayList<String>()
                for (i in searchItemList) {
                    if (i.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(i)
                    }
                }

                if (filteredList.isNotEmpty()) {
                    adapter.setFilteredList(filteredList)
                }
            } else {
                recyclerView.visibility = View.GONE
            }

        }
    }

    private fun searchItemLoadData() {
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {
                if (response.isSuccessful) {
                    var index = 0
                    while (index < response.body()!!.size) {
                        searchItemList.add(response.body()!![index].category)
                        index++
                    }

                }
            }

            override fun onFailure(call: Call<List<ApiProductsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}