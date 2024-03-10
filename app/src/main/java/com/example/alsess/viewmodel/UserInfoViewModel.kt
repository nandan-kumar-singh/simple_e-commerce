package com.example.alsess.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.service.UpdateFirebaseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserInfoViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseFireStoreDB = FirebaseFirestore.getInstance()
    val userInfoMLD = MutableLiveData<HashMap<String, String>>()

    fun readFireStoreUserData() {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!
        val userInfoHashMap = HashMap<String, String>()
        firebaseFireStoreDB.collection("Users").document(currentUser.uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    exception.printStackTrace()
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        val name = snapshot.data?.get("name").toString()
                        val lastName = snapshot.data?.get("lastName").toString()
                        val phone = snapshot.data?.get("phone").toString()
                        userInfoHashMap.put("name", name)
                        userInfoHashMap.put("lastName", lastName)
                        userInfoHashMap.put("phone", phone)
                        userInfoHashMap.put("email", currentUser.email!!)
                        userInfoMLD.value = userInfoHashMap
                    }
                }
            }
    }

    fun updateFireStoreUserData(
        context: Context,
        userName: String,
        userLastName: String,
        userPhone: String
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        val updateFireStore = UpdateFirebaseData(context)

        updateFireStore.updateFirestoreUserInfo(currentUserUid, "name", userName.replaceFirstChar {
            it.uppercase()
        })
        updateFireStore.updateFirestoreUserInfo(
            currentUserUid,
            "lastName",
            userLastName.replaceFirstChar {
                it.uppercase()
            })
        updateFireStore.updateFirestoreUserInfo(currentUserUid, "phone", userPhone)
    }
}