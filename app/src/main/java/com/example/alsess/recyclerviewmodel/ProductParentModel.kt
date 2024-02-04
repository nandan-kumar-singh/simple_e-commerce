package com.example.alsess.recyclerviewmodel

data class ProductParentModel(
    val categoryTitle : String,
    val category : String,
    val childList : List<ProductChildModel>
)
