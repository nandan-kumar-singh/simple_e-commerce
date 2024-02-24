package com.example.alsess.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.FragmentSearchRowBinding
import com.example.alsess.view.SearchFragmentDirections

class SearchAdapter(var searchItemList: List<String>) :
    RecyclerView.Adapter<SearchAdapter.SearchVH>() {
    class SearchVH(val viewBinding: FragmentSearchRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val view =
            FragmentSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVH(view)
    }

    fun setFilteredList(searchItemList: List<String>) {
        this.searchItemList = searchItemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (searchItemList.size > 8) {
            return 7
        } else {
            return searchItemList.size
        }

    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.viewBinding.rowSearchTxvSearchItem.text = searchItemList.get(position)
        holder.viewBinding.rowSearchCardView.setOnClickListener {
            val dataDirections =
                SearchFragmentDirections.actionSearchFragmentToProductSearchFragment(searchItemList.get(position))
            Navigation.findNavController(it)
                .navigate(dataDirections)
        }
    }
}