package com.example.alsess.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.service.AddFirebaseData
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    val loadAuthMLD = MutableLiveData<Boolean>()
    val errorMessageMLD = MutableLiveData<String>()

    fun firebaseAuthentication(
        context: Context,
        email: String,
        password: String,
        name: String,
        lastName: String,
        phone: String
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadAuthMLD.value = true
                    addFirebaseData(context, name, lastName, phone)
                }
            }.addOnFailureListener { exception ->
                errorMessageMLD.value = exception.localizedMessage
            }
    }

    private fun addFirebaseData(context: Context, name: String, lastName: String, phone: String) {
        val addFirebaseData = AddFirebaseData()
        val usersHashMap = HashMap<String, String>()
        usersHashMap.put("name", name)
        usersHashMap.put("lastName", lastName)
        usersHashMap.put("phone", phone)
        addFirebaseData.addFireStoreUserDataSignUp(context, "Users", usersHashMap)
    }
}