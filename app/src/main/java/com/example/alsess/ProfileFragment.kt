package com.example.alsess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.alsess.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var viewBinding : FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)

        val sharedPreferences = context!!.getSharedPreferences("users", Context.MODE_PRIVATE)
        val representative = null

        //Giriş yapıldıktan sonra giriş yapanın bilgileri profile aktarılır
        //Giriş yapan kişi geldiğinde kaydol ve giriş sayfasına dön butonları kaybolup çıkış butonu devreye girer
        if(sharedPreferences.getString("username",representative) != representative){
            Glide.with(context!!).load(sharedPreferences.getString("photo",representative)).into(viewBinding.circleImageView)
            viewBinding.nameText.text = sharedPreferences.getString("name",representative)  + " " + sharedPreferences.getString("surname",representative)
            viewBinding.mailText.text = sharedPreferences.getString("email",representative)
            viewBinding.buttonSingUp.visibility = View.GONE
            viewBinding.returnLoginButton.visibility = View.GONE
        }else{
            viewBinding.exitButton.visibility = View.GONE
            viewBinding.nameText.visibility = View.GONE
            viewBinding.mailText.visibility = View.GONE
            viewBinding.circleImageView.visibility = View.GONE
        }

        // butona basınca sonra giriş yapanın bilgileri silinir
        viewBinding.exitButton.setOnClickListener {
            val sharedPreferences = context!!.getSharedPreferences("users", Context.MODE_PRIVATE)
            val editör = sharedPreferences.edit()
            editör.remove("username")
            editör.remove("pasword")
            editör.remove("id")
            editör.remove("name")
            editör.remove("surname")
            editör.remove("email")
            editör.remove("photo")
            editör.commit()
            val intent = Intent(context, MainActivity :: class.java)
            startActivity(intent)
            activity!!.finish()
        }

        //Misafir modunda giren kişi üye olmak için butona tıklar
        viewBinding.buttonSingUp.setOnClickListener {
            val intent = Intent(context,SingUp :: class.java)
            startActivity(intent)
        }
        //Misafir modunda giren kişi giriş sayfasına gitmek için butona tıklar
        viewBinding.returnLoginButton.setOnClickListener {
            val intent = Intent(context, MainActivity:: class.java)
            startActivity(intent)
            activity!!.finish()
        }
        return viewBinding.root
    }
}