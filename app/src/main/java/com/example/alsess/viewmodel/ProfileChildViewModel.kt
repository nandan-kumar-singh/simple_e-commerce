package com.example.alsess.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.service.AddFirebaseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileChildViewModel : ViewModel() {
    private lateinit var fireBaseAuth: FirebaseAuth
    private val firebaseFireStoreDB = FirebaseFirestore.getInstance()
    val errorMessageMLD = MutableLiveData<String>()
    val loadMLD = MutableLiveData<Boolean>()
    val userMLD = MutableLiveData<HashMap<String, String>>()


    //The user who enters Google for the first time does not have a user information document,
    // this is checked
    fun checkFireStoreDocument() {
        val addFirebaseData = AddFirebaseData()
        fireBaseAuth = FirebaseAuth.getInstance()
        val currentUser = fireBaseAuth.currentUser!!
        firebaseFireStoreDB.collection("Users").document(currentUser.uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists()) {
                            fireStoreUserData()
                        } else {
                            addFirebaseData.addFireStoreUserDataProfile()
                            fireStoreUserData()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }

    //Information received when registering is withdrawn from the profile
    @SuppressLint("SetTextI18n")
    private fun fireStoreUserData() {
        val userHashMap = HashMap<String, String>()
        fireBaseAuth = FirebaseAuth.getInstance()
        val currentUser = fireBaseAuth.currentUser
        if (currentUser != null) {
            firebaseFireStoreDB.collection("Users").document(currentUser.uid)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        errorMessageMLD.value = exception.localizedMessage
                    } else {
                        if (snapshot != null && snapshot.exists()) {
                            val name = snapshot.data?.get("name").toString()
                            val lastName = snapshot.data?.get("lastName").toString()
                            if (name != "" && lastName != "") {
                                userHashMap.put("name", "$name $lastName")
                                userHashMap.put(
                                    "nameChar",
                                    name.get(0).toString() + lastName.get(0).toString()
                                )
                                userMLD.value = userHashMap

                            } else {
                                if (currentUser.displayName != null && currentUser.displayName != "") {
                                    currentUser.displayName?.let { userHashMap.put("name", it) }
                                    userHashMap.put(
                                        "nameChar", currentUser.displayName?.first().toString()
                                    )
                                    userMLD.value = userHashMap
                                } else {
                                    currentUser.displayName?.let { userHashMap.put("name", it) }
                                    userHashMap.put(
                                        "nameChar",
                                        currentUser.email?.get(0).toString().replaceFirstChar {
                                            it.uppercaseChar()
                                        }
                                    )
                                    userMLD.value = userHashMap
                                }
                            }
                            //To prevent the view from arriving before the data arrives
                            loadMLD.value = true
                        }
                    }
                }
        }

    }

}