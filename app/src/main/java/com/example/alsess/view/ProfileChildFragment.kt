package com.example.alsess.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProfileChildBinding
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.viewmodel.ProfileChildViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProfileChildBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var profileChildViewModel: ProfileChildViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileChildBinding.inflate(inflater, container, false)
        fireBaseAuth = FirebaseAuth.getInstance()
        profileChildViewModel = ViewModelProvider(this).get(ProfileChildViewModel::class.java)
        buttonClickAction()
        viewModelObserve()

        //If the user is logged in, the profile is listed, if not, the register or login buttons appear
        if (fireBaseAuth.currentUser != null) {
            profileChildViewModel.checkFireStoreDocument()
        } else {
            viewBinding.fragmentProfileChildBtnLogin.visibility = View.VISIBLE
            viewBinding.fragmentProfileChildBtnSignUp.visibility = View.VISIBLE
        }
        return viewBinding.root

    }

    fun viewModelObserve() {
        profileChildViewModel.errorMessageMLD.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT)
                    .show()
            }

        })
        profileChildViewModel.loadMLD.observe(viewLifecycleOwner, Observer { load ->
            load?.let {
                if (it) {
                    viewBinding.fragmentProfileChildCardViewParent.visibility = View.VISIBLE
                }
            }
        })

        profileChildViewModel.userMLD.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                viewBinding.fragmentProfileChildTxvName.text = it.get("name")
                viewBinding.fragmentProfileChildTxvNameChar.text = it.get("nameChar")
            }
        })

    }

    fun buttonClickAction() {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(requireContext())
        //Transition from profile fragment to other detail fragments
        viewBinding.fragmentProfileChildBtnUserInfo.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileChildFragment_to_userInfoFragment)
        }
        viewBinding.fragmentProfileChildBtnAccountSettings.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileChildFragment_to_accountSettingsFragment)
        }


        //The user logs out, both for those who enter with google and for those who register and enter
        if (fireBaseAuth.currentUser != null) {
            viewBinding.fragmentProfileChildBtnLogOut.setOnClickListener {
                val googleSignInClient =
                    context?.let { it ->
                        GoogleSignIn.getClient(
                            it,
                            GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                    }
                fireBaseAuth.signOut()
                googleSignInClient?.revokeAccess()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                activity?.let(FragmentActivity::finish)
                favoritesDataHelper.writableDatabase.delete("favorites", null, null)
            }
        }

        //transition to sign up and login activity
        viewBinding.fragmentProfileChildBtnLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        viewBinding.fragmentProfileChildBtnSignUp.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}