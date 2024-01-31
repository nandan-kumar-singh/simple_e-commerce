package com.example.alsess

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.alsess.apimodels.ApiUserModel
import com.example.alsess.apis.UserAPI
import com.example.alsess.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        login()
        personRecognition()

        viewBinding.buttonSingUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        viewBinding.buttonQuest.setOnClickListener {
            val intent = Intent(this@MainActivity, HomePage::class.java)
            startActivity(intent)
            finish()
        }

    }
    // Giriş yapılan bilgiler veri tabanından kontrol edilir girildiğinde bilgiler shared preferencese atılır
    fun login() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiLinks.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(UserAPI :: class.java)
        val call = service.loadData()
        call.enqueue(object: Callback<List<ApiUserModel>> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<List<ApiUserModel>>, response: Response<List<ApiUserModel>>) {
                val sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE)
                val editör = sharedPreferences.edit()

                viewBinding.buttonLogin.setOnClickListener {
                    if(response.isSuccessful){
                        val userText = viewBinding.textViewUserName.text.toString()
                        val paswordText = viewBinding.textViewPasword.text.toString()
                        var position = 0
                        while(position<response.body()!!.size){
                            if(userText.equals(response.body()!!.get(position).username ) && paswordText.equals(response.body()!!.get(position).pasword)) {
                                editör.putString("username", userText)
                                editör.putString("pasword", paswordText)
                                editör.putInt("id", response.body()!!.get(position).id)
                                editör.putString("name",response.body()!!.get(position).name )
                                editör.putString("surname",response.body()!!.get(position).surname )
                                editör.putString("photo",response.body()!!.get(position).photo)
                                editör.putString("email",response.body()!!.get(position).email)
                                editör.commit()
                                val intent = Intent(this@MainActivity, HomePage::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                viewBinding.textViewUserName.setBackgroundResource(R.drawable.login_red_edittext_shape)
                                viewBinding.textViewPasword.setBackgroundResource(R.drawable.login_red_edittext_shape)
                            }
                            position ++
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<ApiUserModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    //Girilen verilerin kaydolduğu shared preferencede kontrol edilir ve giren kişi tanındığı an otomatik giriş yapar
    fun personRecognition(){
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiLinks.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(UserAPI :: class.java)
        val call = service.loadData()
        call.enqueue(object : Callback<List<ApiUserModel>>{
            override fun onResponse(call: Call<List<ApiUserModel>>, response: Response<List<ApiUserModel>>, ) {
                val sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE)
                var indeks = 0
                while (indeks <response.body()!!.size){
                    if(sharedPreferences.getString("username","") == response.body()!!.get(indeks).username && sharedPreferences.getString("pasword","") == response.body()!!.get(indeks).pasword){
                        val intent = Intent(this@MainActivity, HomePage::class.java)
                        startActivity(intent)
                        finish()
                    }
                    indeks ++
                }
            }
            override fun onFailure(call: Call<List<ApiUserModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}