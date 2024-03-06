package com.example.alsess.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.alsess.R
import com.example.alsess.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        auth = FirebaseAuth.getInstance()

    }

    //activitySignUpButtonSingUp onClick
    //when all the information is entered, the user is registered in the system
    fun signUp(view : View) {
        val name = viewBinding.activitySignUpEdtName.text.toString()
        val lastName = viewBinding.activitySignUpEdtLastName.text.toString()
        val email = viewBinding.activitySignUpEdtEMail.text.toString()
        val phone = viewBinding.activitySignUpEdtPhone.text.toString()
        val password = viewBinding.activitySignUpEdtPassword.text.toString()
        val passwordAgain = viewBinding.activitySignUpEdtPasswordAgain.text.toString()
        if (name != "" && lastName != "" && email != "" && phone != "" && password != "" && passwordAgain != "") {
            if (password == passwordAgain) {
                firebaseAuthentication()
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

    //firebase Authentication
    fun firebaseAuthentication() {
        val email = viewBinding.activitySignUpEdtEMail.text.toString()
        val password = viewBinding.activitySignUpEdtPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewBinding.activitySignUpProgressBar.visibility = View.VISIBLE
                    firebaseFirestore()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    //User data is saved in firestore
    /*The password will be used in the password changing section,
    the product ID will be used for favorites and product IDs added to the cart. */
    private fun firebaseFirestore() {
        val name = viewBinding.activitySignUpEdtName.text.toString().trim().replaceFirstChar {
            it.uppercase()
        }
        val lastName =
            viewBinding.activitySignUpEdtLastName.text.toString().trim().replaceFirstChar {
                it.uppercase()
            }
        val phone = viewBinding.activitySignUpEdtPhone.text.toString().trim()
        val usersHashMap = HashMap<String, Any>()
        usersHashMap.put("name", name)
        usersHashMap.put("lastName", lastName)
        usersHashMap.put("phone", phone)
        firebaseFirestoreCollection("Users", usersHashMap)


    }

    //Called when adding multiple collections
    fun firebaseFirestoreCollection(collectionName: String, setHashMap: HashMap<String, Any>) {
        val currentUserUid = auth.currentUser!!.uid
        firebaseFirestoreDB.collection(collectionName).document(currentUserUid).set(setHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }

}