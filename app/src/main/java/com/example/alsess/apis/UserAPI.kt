package com.example.alsess.apis

import com.example.alsess.ApiLinks
import com.example.alsess.apimodels.ApiUserModel
import retrofit2.Call
import retrofit2.http.GET

interface UserAPI {
    @GET(ApiLinks.USER_CONTİNUANCE_URL)
    fun loadData() : Call<List<ApiUserModel>>
}