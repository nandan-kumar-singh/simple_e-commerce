package com.example.alsess.model

data class BasketSQLiteModel(
    var id: Long,
    var title: String,
    var price: Double,
    var imageUrl: String,
    var count: Int
)
