package com.example.alsess.adapters

import com.example.alsess.model.BasketSQLiteModel

interface OnChangeAmount {
    fun onChange(totalPrice: String, finalList: List<BasketSQLiteModel>);
}