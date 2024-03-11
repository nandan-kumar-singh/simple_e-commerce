package com.example.alsess.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alsess.R
import com.example.alsess.databinding.ActivityLoginBinding
import com.example.alsess.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.request(this@LoginActivity)
        viewModelObserve()
    }

    //activityLoginButtonLogin onClick
    //Login with e-mail and password using Firebase
    fun loginWithEmail(view: View) {
        val email = viewBinding.activityLoginTxvEmail.text.toString().trim()
        val password = viewBinding.activityLoginTxvPassword.text.toString().trim()
        loginViewModel.loginWithEmail(email, password)
    }

    //activityLoginButtonResetPassword onClick
    //Firebase password reset link is sent to your e-mail
    fun resetPassword(view: View) {
        val email = viewBinding.activityLoginTxvEmail.text.toString().trim()
        loginViewModel.resetPassword(email)
    }

    //activityLoginImageButtonGoogle onClick
    //Login with Google using Firebase
    fun loginWithGoogle(view: View) {
        loginViewModel.signInGoogle(launcher)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                loginViewModel.handleResults(task)
            }
        }

    private fun viewModelObserve() {
        loginViewModel.loginMLD.observe(this, Observer { login ->
            login?.let {
                if (it) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        })
        loginViewModel.errorMessageMLD.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                    .show()

            }
        })
        loginViewModel.loginEmptyMLD.observe(this, Observer { loginEmpty ->
            loginEmpty?.let {
                if (it) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.email_password_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        loginViewModel.resetLinkMLD.observe(this, Observer { reset ->
            reset?.let {
                if (it) {
                    viewBinding.activityLoginTxvPassword.setText("")
                }
            }
        })

        loginViewModel.resetErrorMessageMLD.observe(this, Observer { resetError ->
            resetError?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                    .show()
            }
        })

        loginViewModel.resetEmptyMLD.observe(this, Observer { resetEmpty ->
            resetEmpty?.let {
                if (it) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.enter_email),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        loginViewModel.handleErrorMLD.observe(this, Observer { handleError ->
            handleError?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.loginWitGoogleMLD.observe(this, Observer { handleError ->
            handleError?.let {
                if (it) {
                    viewBinding.activityLoginPgb.visibility = View.VISIBLE
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        })
        loginViewModel.updateUIErrorMessageMLD.observe(this, Observer { updateUIError ->
            updateUIError?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

    }
}