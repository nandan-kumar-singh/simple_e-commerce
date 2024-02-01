package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserInfoFragment : Fragment() {
    private lateinit var viewBinding: FragmentUserInfoBinding
    private lateinit var auth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentUserInfoBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        readFirestoreUserData()

        viewBinding.fragmentUserInfoToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewBinding.fragmentUserInfoBtnUpdateInfo.setOnClickListener {
            ubdateFirestoreUserData()
        }

        return viewBinding.root
    }

    //User information is retrieved from firestore
    fun readFirestoreUserData() {
        val currentUser = auth.currentUser!!
        viewBinding.fragmentUserInfoTxvEmail.text = currentUser.email
        firebaseFirestoreDB.collection("Users").document(currentUser.uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        val name = snapshot.data?.get("name").toString()
                        val lastName = snapshot.data?.get("lastName").toString()
                        val phone = snapshot.data?.get("phone").toString()
                        val emptyEditTextShape = R.drawable.edittext_empty_shape
                        val editTextShape = R.drawable.edittext_shape
                        if (name != "") {
                            viewBinding.fragmentUserInfoEdtName.setText(name)
                            viewBinding.fragmentUserInfoEdtName.setBackgroundResource(
                                editTextShape
                            )
                        } else {
                            viewBinding.fragmentUserInfoEdtName.setBackgroundResource(
                                emptyEditTextShape
                            )
                        }
                        if (lastName != "") {
                            viewBinding.fragmentUserInfoEdtLastName.setText(lastName)
                            viewBinding.fragmentUserInfoEdtLastName.setBackgroundResource(
                                editTextShape
                            )
                        } else {
                            viewBinding.fragmentUserInfoEdtLastName.setBackgroundResource(
                                emptyEditTextShape
                            )
                        }
                        if (phone != "") {
                            viewBinding.fragmentUserInfoEdtPhone.setText(phone)
                            viewBinding.fragmentUserInfoEdtPhone.setBackgroundResource(
                                editTextShape
                            )
                        } else {
                            viewBinding.fragmentUserInfoEdtPhone.setBackgroundResource(
                                emptyEditTextShape
                            )
                        }
                    }
                }
            }
    }

    //Updates according to the entered field
    fun ubdateFirestoreUserData() {
        val userName = viewBinding.fragmentUserInfoEdtName.text.toString()
        val userLastName = viewBinding.fragmentUserInfoEdtLastName.text.toString()
        val userPhone = viewBinding.fragmentUserInfoEdtPhone.text.toString()
        val currentUserUid = auth.currentUser!!.uid
        val updateFirestore = UpdateFirestoreData(requireContext())

        updateFirestore.updateFirestoreUserInfo(currentUserUid, "name", userName)
        updateFirestore.updateFirestoreUserInfo(currentUserUid, "lastName", userLastName)
        updateFirestore.updateFirestoreUserInfo(currentUserUid, "phone", userPhone)
    }
}