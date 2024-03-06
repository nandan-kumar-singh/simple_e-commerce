package com.example.alsess.service


import android.content.Context
import android.widget.Toast
import com.example.alsess.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateFirebaseData(val context: Context) {
    private val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth

    fun updateFirestoreUserInfo(currentUserUid: String, field: String, value: String) {
        firebaseFirestoreDB.collection("Users").document(currentUserUid)
            .update(field, value).addOnSuccessListener {
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


    //To change email via Firebase
    //NOTE : Not working due to Firebase restriction
    fun changefirebaseEmail(newEmail: String, newEmailAgain: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!

        if (newEmail != "" && newEmailAgain != "") {
            if (newEmail == newEmailAgain) {
                currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.change_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.email_not_same),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(context, context.getString(R.string.empty_field), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun changefirebasePassword(newPassword: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            it.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_updated),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
        }
    }
}