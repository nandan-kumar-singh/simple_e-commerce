package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentChangeEmailBinding
import com.google.firebase.auth.FirebaseAuth

class ChangeEmailFragment : Fragment() {
    private lateinit var viewBinding: FragmentChangeEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChangeEmailBinding.inflate(inflater, container, false)

        return viewBinding.root
    }
}