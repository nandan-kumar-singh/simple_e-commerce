package com.example.alsess.service

import com.example.alsess.BuildConfig
import com.example.alsess.MyApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductsRetrofitService {
    var retrofit: Retrofit

    init {
        val client =
            OkHttpClient().newBuilder().addInterceptor(MockIntercepter(context = MyApp.context))
                .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val service = retrofit.create(ProductsAPI::class.java)
}