package com.example.productsapp.repository

import com.example.productsapp.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class ProductsRepository {
    suspend fun fetchProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            val response = URL("https://dummyjson.com/products?limit=100").readText()
            val json = JSONObject(response)
            val productsJson = json.getJSONArray("products")
            val products = mutableListOf<Product>()

            for (i in 0 until productsJson.length()) {
                val item = productsJson.getJSONObject(i)
                products.add(
                    Product(
                        id = item.getInt("id"),
                        title = item.getString("title"),
                        price = item.getDouble("price"),
                        stock = item.getInt("stock"),
                        category = item.getString("category"),
                        thumbnail = item.getString("thumbnail")
                    )
                )
            }
            products
        }
    }
}
