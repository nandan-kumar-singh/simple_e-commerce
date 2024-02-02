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
    private lateinit var firebaaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        firebaaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaaseAuth.currentUser!!
        viewBinding.fragmentChangeEmailTxvCurrentEmail.text = currentUser.email

        if (currentUser.isEmailVerified) {
            viewBinding.fragmentChangeEmailCardView.visibility = View.VISIBLE
        } else {
            viewBinding.fragmentChangeEmailTxvVerifyYourEmail.visibility = View.VISIBLE
            viewBinding.fragmentChangeEmailTxvGo.visibility = View.VISIBLE
        }

        viewBinding.fragmentChangeEmailBtnChange.setOnClickListener {
            changefirebaseEmail()
        }

        viewBinding.fragmentChangeEmailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        return viewBinding.root
    }

    //To change email via Firebase
    //NOTE : Not working due to Firebase restriction
    fun changefirebaseEmail() {
        val currentUser = firebaaseAuth.currentUser!!
        val newEmail = viewBinding.fragmentChangeEmailEdtNewEmail.text.toString()
        val newEmailAgain = viewBinding.fragmentChangeEmailEdtNewEmailAgain.text.toString()

        if (newEmail != "" && newEmailAgain != "") {
            if (newEmail == newEmailAgain) {
                currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            getString(R.string.change_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.email_not_same), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, getString(R.string.empty_field), Toast.LENGTH_SHORT).show()
        }
    }
}