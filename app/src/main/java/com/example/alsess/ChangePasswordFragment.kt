package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordFragment : Fragment() {
    private lateinit var viewBinding: FragmentChangePasswordBinding
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}