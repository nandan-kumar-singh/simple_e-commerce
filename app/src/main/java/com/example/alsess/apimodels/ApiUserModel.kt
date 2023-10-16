package com.example.alsess.apimodels

data class ApiUserModel(
    val id : Int,
    val name: String,
    val surname: String,
    val email: String,
    val username: String,
    val pasword: String,
    val photo: String
)
