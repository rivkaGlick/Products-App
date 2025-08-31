package com.example.productsapp.model

// Data class representing a single product from the API
data class Product(
    val id: Int,               // Unique product ID
    val title: String,         // Product name/title
    val price: Double,         // Price of the product
    val stock: Int,            // Quantity available in stock
    val category: String,      // Category this product belongs to
    val thumbnail: String      // URL of the product image
)
