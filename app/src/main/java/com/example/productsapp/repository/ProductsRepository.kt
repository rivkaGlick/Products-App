package com.example.productsapp.repository

import com.example.productsapp.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// Sealed class to represent success or error result
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()        // Holds successful data
    data class Error(val exception: Exception) : Result<Nothing>() // Holds error info
}

class ProductsRepository {

    // Local cache to avoid unnecessary network requests
    private var cachedProducts: List<Product>? = null
    private var lastFetchTime: Long = 0
    private val cacheTTL = 5 * 60 * 1000L // 5 minutes

    // Fetch product list, optionally forcing a refresh
    suspend fun fetchProducts(forceRefresh: Boolean = false): Result<List<Product>> {
        val now = System.currentTimeMillis()

        // Return cached products if still valid and no force refresh
        if (!forceRefresh && cachedProducts != null && now - lastFetchTime < cacheTTL) {
            return Result.Success(cachedProducts!!)
        }

        return try {
            // Network call on IO dispatcher
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

            // Update cache
            cachedProducts = products
            lastFetchTime = now

            Result.Success(products)
        } catch (e: Exception) {
            // Return error if network request fails or parsing fails
            Result.Error(e)
        }
    }
}
