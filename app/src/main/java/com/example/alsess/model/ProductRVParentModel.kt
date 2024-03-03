package com.example.alsess.model

data class ProductRVParentModel(
    val categoryTitle: String,
    val category: String,
    val childList: List<ProductRVChildModel>
)
