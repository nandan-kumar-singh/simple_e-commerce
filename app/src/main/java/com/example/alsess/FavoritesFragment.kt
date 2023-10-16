package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.adapters.FavoritesReyclerViewAdapter
import com.example.alsess.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private lateinit var viewBinding : FragmentFavoritesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        viewBinding.RecyclerView.adapter = context?.let { FavoritesReyclerViewAdapter(it) }
        viewBinding.RecyclerView.layoutManager = GridLayoutManager(context,2)
        return viewBinding.root
    }
}