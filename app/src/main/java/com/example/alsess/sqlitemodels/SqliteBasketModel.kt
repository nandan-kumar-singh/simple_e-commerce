package com.example.alsess.sqlitemodels

data class SqliteBasketModel(
    var id: Long,
    var title: String,
    var price: Double,
    var imageUrl: String,
    var count: Int
)