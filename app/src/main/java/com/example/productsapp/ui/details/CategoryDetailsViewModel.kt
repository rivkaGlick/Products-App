package com.example.productsapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.model.Product
import com.example.productsapp.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryDetailsViewModel : ViewModel() {

    private val repo = ProductsRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    fun loadCategory(category: String) {
        viewModelScope.launch {
            val allProducts = repo.fetchProducts()
            _products.value = allProducts.filter { it.category == category }
        }
    }
}
