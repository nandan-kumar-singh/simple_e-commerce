package com.example.alsess


import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class UpdateFirestoreData(val context: Context) {
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    fun updateFirestoreUserInfo(currentUserUid: String, field: String, value: String) {
        firebaseFirestoreDB.collection("Users").document(currentUserUid)
            .update(field, value).addOnSuccessListener {
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}