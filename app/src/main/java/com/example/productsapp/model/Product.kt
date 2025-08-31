package com.example.productsapp.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val thumbnail: String
)
