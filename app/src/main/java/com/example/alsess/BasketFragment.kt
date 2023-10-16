package com.example.alsess


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alsess.adapters.BasketRecyclerViewAdapter
import com.example.alsess.databinding.FragmentBasketBinding



class BasketFragment : Fragment() {
    private lateinit var viewBinding : FragmentBasketBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewBinding = FragmentBasketBinding.inflate(inflater, container, false)

        val sharedPreferences = context?.getSharedPreferences("totalPrice",Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        //fiyat totallerini anlık olarak almak ve değişiklikleri anında vermek için kullanılır
        val onChangeAmount = object : OnChangeAmount {
            override fun onChange(totalPrice: String) {
                viewBinding.totalPrice.text = totalPrice
                editor?.putFloat("total",totalPrice.toFloat())
                editor?.apply()
                if(totalPrice.toFloat() == 0F){
                    viewBinding.cardView.visibility = View.GONE

                }else{
                    viewBinding.cardView.visibility = View.VISIBLE
                }
            }
        }

        if(sharedPreferences!!.getFloat("total",0.0F ) ==0.0F){
            viewBinding.cardView.visibility = View.GONE
        }
        viewBinding.totalPrice.text = sharedPreferences?.getFloat("total",0F).toString()

        viewBinding.recyclerView.adapter = context?.let { BasketRecyclerViewAdapter(it,onChangeAmount)}
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        return viewBinding.root
    
        }

    }
