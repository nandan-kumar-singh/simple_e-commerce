package com.example.alsess.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.service.ProductsRetrofitService
import com.example.alsess.R
import com.example.alsess.adapters.ProductCategoryAdapter
import com.example.alsess.databinding.FragmentProductCategoryBinding
import com.example.alsess.model.ApiProductsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class ProductCategoryFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductCategoryBinding
    val productMutableList: MutableList<ApiProductsModel> = mutableListOf()
    val bundle: ProductCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProductCategoryBinding.inflate(inflater, container, false)

        productLoadData()
        recyclerViewActions()

        if (productMutableList.size != 0) {
            viewBinding.fragmentProductCategoryAllPgb.visibility = View.GONE
            viewBinding.fragmentProductCategoryAllBtnSort.visibility = View.VISIBLE
        }
        viewBinding.fragmentProductCategoryAllToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewBinding.fragmentProductCategoryAllBtnSort.setOnClickListener {
            bottomSheetDialogAction()
        }
        return viewBinding.root
    }

    fun productLoadData() {
        val retrofit = ProductsRetrofitService()
        retrofit.service.loadData().enqueue(object : Callback<List<ApiProductsModel>> {
            override fun onResponse(
                call: Call<List<ApiProductsModel>>,
                response: Response<List<ApiProductsModel>>
            ) {

                if (response.isSuccessful) {
                    viewBinding.fragmentProductCategoryAllPgb.visibility = View.GONE
                    viewBinding.fragmentProductCategoryAllBtnSort.visibility = View.VISIBLE
                    if (productMutableList.size == 0) {
                        recyclerViewActions()
                    }
                    //The listing changes depending on which category is clicked
                    var indeks = 0
                    if(productMutableList.size == 0){
                        while (indeks < response.body()!!.size) {
                            if (bundle.category == response.body()!!.get(indeks).category) {
                                productMutableList.add(
                                    ApiProductsModel(
                                        response.body()!!.get(indeks).id,
                                        response.body()!!.get(indeks).title,
                                        response.body()!!.get(indeks).price,
                                        response.body()!!.get(indeks).description,
                                        response.body()!!.get(indeks).category,
                                        response.body()!!.get(indeks).image,
                                        response.body()!!.get(indeks).rating
                                    )
                                )


                            }
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


    @SuppressLint("CommitPrefEdits", "NotifyDataSetChanged")
    fun bottomSheetDialogAction() {
        val bottomSheetDialog = BottomSheetDialog(context!!)
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_category_bottom_sheet, null)
        val sharedPreferences =
            context!!.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()

        val imbCancel = bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetImbCancel) as ImageButton

        val radioGroup =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRg) as RadioGroup
        bottomSheetDialog.setContentView(bottomSheetView)

        val radioButton1 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbDefaultSorting) as RadioButton
        val radioButton2 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbLowestPrice) as RadioButton
        val radioButton3 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbHighestPrice) as RadioButton
        val radioButton4 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbLowestRated) as RadioButton
        val radioButton5 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbHighestRated) as RadioButton

        //Radio button remembers the saved click id
        when (sharedPreferences.getInt("rbClickId", 0)) {
            1 -> radioButton1.isChecked = true
            2 -> radioButton2.isChecked = true
            3 -> radioButton3.isChecked = true
            4 -> radioButton4.isChecked = true
            5 -> radioButton5.isChecked = true
        }

        //Select the category via Radio Button and save the click id in sharedpreferences
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.fragmentCategoryBottomSheetRbDefaultSorting -> {
                    sharedPreferencesEditor.putInt("rbClickId", 1)
                    sharedPreferencesEditor.apply()
                    if (productMutableList.size != 0) {
                        recyclerViewActions()
                        productMutableList.sortBy {
                            it.id
                        }
                        bottomSheetDialog.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbLowestPrice -> {
                    sharedPreferencesEditor.putInt("rbClickId", 2)
                    sharedPreferencesEditor.apply()
                    if (productMutableList.size != 0) {
                        recyclerViewActions()
                        productMutableList.sortBy {
                            it.price
                        }
                        bottomSheetDialog.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbHighestPrice -> {
                    sharedPreferencesEditor.putInt("rbClickId", 3)
                    sharedPreferencesEditor.apply()
                    if (productMutableList.size != 0) {
                        recyclerViewActions()
                        productMutableList.sortBy {
                            it.price
                        }
                        Collections.reverse(productMutableList)
                        bottomSheetDialog.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbLowestRated -> {
                    sharedPreferencesEditor.putInt("rbClickId", 4)
                    sharedPreferencesEditor.apply()
                    if (productMutableList.size != 0) {
                        recyclerViewActions()
                        productMutableList.sortBy {
                            it.rating.rate
                        }
                        bottomSheetDialog.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbHighestRated -> {
                    sharedPreferencesEditor.putInt("rbClickId", 5)
                    sharedPreferencesEditor.apply()
                    if (productMutableList.size != 0) {
                        recyclerViewActions()
                        productMutableList.sortBy {
                            it.rating.rate
                        }
                        Collections.reverse(productMutableList)
                        bottomSheetDialog.dismiss()
                    }

                }
            }
        }

        imbCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

    }

    fun recyclerViewActions() {
        viewBinding.fragmentProductCategoryAllRecyclerView.adapter =
            context?.let {
                ProductCategoryAdapter(
                    it,
                    productMutableList
                )
            }
        viewBinding.fragmentProductCategoryAllRecyclerView.layoutManager =
            GridLayoutManager(context, 2)
        viewBinding.fragmentProductCategoryAllToolbar.title = bundle.categoryTitle
    }
}