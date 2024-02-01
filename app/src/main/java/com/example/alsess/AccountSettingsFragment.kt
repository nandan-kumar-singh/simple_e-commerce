package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : Fragment() {
    private lateinit var viewBinding: FragmentAccountSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAccountSettingsBinding.inflate(inflater, container, false)

        return viewBinding.root
    }
}