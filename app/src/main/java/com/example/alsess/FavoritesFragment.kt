package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alsess.adapters.FavoritesReyclerViewAdapter
import com.example.alsess.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private lateinit var viewBinding : FragmentFavoritesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        viewBinding.fragmentFavoritesRecyclerView.adapter = context?.let { FavoritesReyclerViewAdapter(it) }
        viewBinding.fragmentFavoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        return viewBinding.root
    }
}