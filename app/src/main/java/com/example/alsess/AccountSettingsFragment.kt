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

        buttonClickAction()

        return viewBinding.root
    }

    fun buttonClickAction() {
        viewBinding.fragmentAccountSettingsToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewBinding.fragmentAccountSettingsBtnChangeEmail.setOnClickListener {
            navigations(it, R.id.action_accountSettingsFragment_to_changeEmailFragment)
        }

        viewBinding.fragmentAccountSettingsBtnChangePassword.setOnClickListener {
            navigations(it, R.id.action_accountSettingsFragment_to_changePasswordFragment)
        }

        viewBinding.fragmentAccountSettingsBtnEmailverification.setOnClickListener {
            navigations(it, R.id.action_accountSettingsFragment_to_emailVerificationFragment)
        }

        viewBinding.fragmentAccountSettingsBtnDellete.setOnClickListener {
            navigations(it, R.id.action_accountSettingsFragment_to_disableDeleteAccountFragment)
        }
    }

    fun navigations(view: View, actionId: Int) {
        Navigation.findNavController(view).navigate(actionId)
    }
}