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

    private val repository = ProductsRepository() // Responsible for fetching products

    // List of products for the selected category
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Error message
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // Load products for a specific category
    fun loadProducts(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.fetchProducts()) {
                is Result.Success -> {
                    _products.value = result.data.filter { it.category == categoryName }
                    _error.value = null
                }
                is Result.Error -> {
                    _products.value = emptyList()
                    _error.value = result.exception.message
                }
            }

            _isLoading.value = false
        }
    }
}
