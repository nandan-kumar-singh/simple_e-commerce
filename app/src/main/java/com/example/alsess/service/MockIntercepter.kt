package com.example.alsess.service

import android.content.Context
import android.content.res.AssetManager
import com.example.alsess.MyApp
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException


class MockIntercepter(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        val url: HttpUrl = chain.request().url()
        when (url.encodedPath()) {
            "/products" -> {
                val response: String = context.assets.readAssetsFile("products.json")
                return Response.Builder()
                    .code(200)
                    .message(response)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        ResponseBody.create(
                            MediaType.parse("application/json"),
                            response.toByteArray()
                        )
                    )
                    .addHeader("content-type", "application/json")
                    .build()
            }
            else-> {
                url
            }

        }
        return chain.proceed(currentRequest.build())
    }

    fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}
}