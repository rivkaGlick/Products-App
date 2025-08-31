package com.example.productsapp.repository

import com.example.productsapp.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// Sealed class to represent Success / Error result
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>() // Successful result with data
    data class Error(val exception: Exception) : Result<Nothing>() // Error result with exception
}

class ProductsRepository {

    // Cache for storing products locally
    private var cachedProducts: List<Product>? = null
    private var lastFetchTime: Long = 0
    private val cacheTTL = 5 * 60 * 1000L // Cache Time-To-Live: 5 minutes

    // Fetch products from API, with optional forced refresh
    suspend fun fetchProducts(forceRefresh: Boolean = false): Result<List<Product>> {
        val now = System.currentTimeMillis()

        // Return cached data if valid and no forced refresh
        if (!forceRefresh && cachedProducts != null && now - lastFetchTime < cacheTTL) {
            return Result.Success(cachedProducts!!)
        }

        return try {
            // Network call should run on IO dispatcher
            val response = withContext(Dispatchers.IO) {
                URL("https://dummyjson.com/products?limit=100").readText()
            }

            // Parse JSON response
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

            // Update cache and timestamp
            cachedProducts = products
            lastFetchTime = now

            Result.Success(products) // Return successful result
        } catch (e: Exception) {
            Result.Error(e) // Return error result
        }
    }
}
