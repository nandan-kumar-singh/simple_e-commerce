package com.example.alsess.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProfileChildBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProfileChildBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileChildBinding.inflate(inflater, container, false)
        fireBaseAuth = FirebaseAuth.getInstance()
        buttonClickAction()


        //If the user is logged in, the profile is listed, if not, the register or login buttons appear
        if (fireBaseAuth.currentUser != null) {
            checkFirestoredocument()
        } else {
            viewBinding.fragmentProfileChildBtnLogin.visibility = View.VISIBLE
            viewBinding.fragmentProfileChildBtnSignUp.visibility = View.VISIBLE
        }
        return viewBinding.root

    }

    fun buttonClickAction() {
        //Transition from profile fragment to other detail fragments
        viewBinding.fragmentProfileChildBtnUserInfo.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileChildFragment_to_userInfoFragment)
        }
        viewBinding.fragmentProfileChildBtnAccountSettings.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileChildFragment_to_accountSettingsFragment)
        }


        //The user logs out, both for those who enter with google and for those who register and enter
        if (fireBaseAuth.currentUser != null) {
            viewBinding.fragmentProfileChildBtnLogOut.setOnClickListener {
                val googleSignInClient =
                    context?.let { it1 -> GoogleSignIn.getClient(it1, GoogleSignInOptions.DEFAULT_SIGN_IN) }
                fireBaseAuth.signOut()
                googleSignInClient?.revokeAccess()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                activity?.let(FragmentActivity::finish)
            }
        }

        //transition to sign up and login activity
        viewBinding.fragmentProfileChildBtnLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        viewBinding.fragmentProfileChildBtnSignUp.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    //The user who enters Google for the first time does not have a user information document,
    // this is checked
    fun checkFirestoredocument() {
        val currentUser = fireBaseAuth.currentUser!!
        firebaseFirestoreDB.collection("Users").document(currentUser.uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists() || document == null) {
                            firestoreUserData()
                        } else {
                            firebaseFirestoreAddData()
                            viewBinding.fragmentProfileChildTxvName.text =
                                currentUser.displayName
                            viewBinding.fragmentProfileChildTxvNameChar.text =
                                currentUser.displayName?.get(0)
                                    .toString()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }

    //Information received when registering is withdrawn from the profile
    @SuppressLint("SetTextI18n")
    fun firestoreUserData() {
        val currentUser = fireBaseAuth.currentUser
        if (currentUser != null) {
            firebaseFirestoreDB.collection("Users").document(currentUser.uid)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (snapshot != null && snapshot.exists()) {
                            val name = snapshot.data?.get("name").toString()
                            val lastName = snapshot.data?.get("lastName").toString()
                            if (name != "" && lastName != "") {
                                viewBinding.fragmentProfileChildTxvName.text = "$name $lastName"
                                viewBinding.fragmentProfileChildTxvNameChar.text =
                                    name.get(0).toString() + lastName.get(0).toString()
                            } else {
                                if (currentUser.displayName != null && currentUser.displayName != "") {
                                    viewBinding.fragmentProfileChildTxvName.text =
                                        currentUser.displayName
                                    viewBinding.fragmentProfileChildTxvNameChar.text =
                                        currentUser.displayName?.first().toString()
                                } else {
                                    viewBinding.fragmentProfileChildTxvName.text = currentUser.email
                                    viewBinding.fragmentProfileChildTxvNameChar.text =
                                        currentUser.email?.get(0).toString().replaceFirstChar {
                                            it.uppercaseChar()
                                        }
                                }
                            }
                            //To prevent the view from arriving before the data arrives
                            viewBinding.fragmentProfileChildCardViewParent.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }

    //Create user information document if it does not exist
    fun firebaseFirestoreAddData() {
        val currentUserUid = fireBaseAuth.currentUser!!.uid
        val usersHashMap = HashMap<String, Any>()
        usersHashMap.put("name", "")
        usersHashMap.put("lastName", "")
        usersHashMap.put("phone", "")
        firebaseFirestoreDB.collection("Users").document(currentUserUid).set(usersHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }
}