package com.example.alsess.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alsess.R
import com.example.alsess.databinding.ActivitySignUpBinding
import com.example.alsess.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        viewModelObserve()
    }

    //activitySignUpButtonSingUp onClick
    //when all the information is entered, the user is registered in the system
    fun signUp(view: View) {
        val email = viewBinding.activitySignUpEdtEMail.text.toString()
        val password = viewBinding.activitySignUpEdtPassword.text.toString()
        val passwordAgain = viewBinding.activitySignUpEdtPasswordAgain.text.toString()
        val name = viewBinding.activitySignUpEdtName.text.toString().trim().replaceFirstChar {
            it.uppercase()
        }
        val lastName =
            viewBinding.activitySignUpEdtLastName.text.toString().trim().replaceFirstChar {
                it.uppercase()
            }
        val phone = viewBinding.activitySignUpEdtPhone.text.toString().trim()

        if (name != "" && lastName != "" && email != "" && phone != "" && password != "" && passwordAgain != "") {
            if (password == passwordAgain) {
                signUpViewModel.firebaseAuthentication(
                    this@SignUpActivity,
                    email,
                    password,
                    name,
                    lastName,
                    phone
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.password_not_same),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(applicationContext, getString(R.string.empty_field), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun viewModelObserve() {
        signUpViewModel.loadAuthMLD.observe(this, Observer { load ->
            load?.let {
                if (it) {
                    viewBinding.activitySignUpProgressBar.visibility = View.VISIBLE
                }
            }
        })

        signUpViewModel.errorMessageMLD.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }
}