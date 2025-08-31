package com.example.productsapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.model.Product
import com.example.productsapp.repository.ProductsRepository
import com.example.productsapp.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryDetailsViewModel : ViewModel() {

    // Repository instance for fetching products
    private val repo = ProductsRepository()

    // StateFlow for the list of products
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    // StateFlow for error messages
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // StateFlow to indicate loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Load products for a given category
    fun loadProducts(category: String) {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            when (val result = repo.fetchProducts()) {
                is Result.Success -> {
                    // Filter products for the selected category
                    _products.value = result.data.filter { it.category == category }
                    _error.value = null // Clear any previous errors
                }
                is Result.Error -> {
                    // Set products to empty list and store error message
                    _products.value = emptyList()
                    _error.value = result.exception.message
                }
            }
            _isLoading.value = false // Loading finished
        }
    }
}
