package com.example.alsess

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentDeleteAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeleteAccountFragment : Fragment() {
    private lateinit var viewBinding: FragmentDeleteAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        viewBinding.fragmentDeleteAccountToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            viewBinding.fragmentDeleteAccountEdtPassword.visibility = View.GONE
        }else{
            viewBinding.fragmentDeleteAccountEdtPassword.visibility = View.VISIBLE
        }
        viewBinding.fragmentDeleteAccountBtnDelete.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
                controlloginWithGoogleEmail()
            }else{
                controlPasswordInfo()
            }

        }
        return viewBinding.root
    }

    //Click on the button and it exits and is deleted from firestore and authtan
    fun deleteAccoundAlerdDialog() {
        val currendUser = firebaseAuth.currentUser!!
        val alertDialog = android.app.AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.delete_account))
        alertDialog.setMessage(getString(R.string.delete_account_alert_message))
        alertDialog.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
            alertDialog.create().dismiss()
        }
        alertDialog.setNeutralButton(getString(R.string.yes)) { dialogInterface, i ->
            currendUser.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /*After deleting someone who enters Google,
                     so that the selection screen appears again on the login screen
                     */
                    val googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
                    googleSignInClient.revokeAccess()

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    firebaseFirestoreDB.collection("Users").document(currendUser.uid).delete()
                    firebaseFirestoreDB.collection("Password").document(currendUser.uid).delete()
                    firebaseFirestoreDB.collection("Product Id").document(currendUser.uid).delete()
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.show()

    }

    //check the correctness of the data entered in the edittext
    fun controlPasswordInfo() {
        val currentUser = firebaseAuth.currentUser!!
        val userEmail = viewBinding.fragmentDeleteAccountEdtEmail.text.toString()
        val userPassword = viewBinding.fragmentDeleteAccountEdtPassword.text.toString()
        firebaseFirestoreDB.collection("Password").document(currentUser.uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        val password = snapshot.data?.get("password").toString()
                        if (currentUser.email == userEmail && password == userPassword) {
                            deleteAccoundAlerdDialog()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.enter_info_correctly),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
    }
    //Separate control for entering with google
    fun controlloginWithGoogleEmail(){
        val currentUser = firebaseAuth.currentUser!!
        val userEmail = viewBinding.fragmentDeleteAccountEdtEmail.text.toString()
        if (currentUser.email == userEmail) {
            deleteAccoundAlerdDialog()
        } else {
            Toast.makeText(
                context,
                getString(R.string.enter_info_correctly),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}
