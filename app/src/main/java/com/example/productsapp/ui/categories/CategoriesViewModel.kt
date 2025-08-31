package com.example.productsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.repository.ProductsRepository
import com.example.productsapp.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Model representing a category for the UI
data class CategoryUI(
    val name: String,
    val thumbnail: String,
    val itemCount: Int,
    val totalStock: Int
)

class CategoriesViewModel : ViewModel() {

    private val repository = ProductsRepository() // fetch products

    // List of categories for UI
    private val _categoryList = MutableStateFlow<List<CategoryUI>>(emptyList())
    val categoryList: StateFlow<List<CategoryUI>> get() = _categoryList

    // Loading state
    private val _loading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _loading

    // Error message
    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> get() = _loadError

    // Load categories, optionally forcing refresh
    fun fetchCategories(forceReload: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true

            when (val result = repository.fetchProducts(forceReload)) {
                is Result.Success -> {
                    val grouped = result.data.groupBy { it.category }
                    _categoryList.value = grouped.map { (categoryName, items) ->
                        CategoryUI(
                            name = categoryName,
                            thumbnail = items.first().thumbnail,
                            itemCount = items.size,
                            totalStock = items.sumOf { it.stock }
                        )
                    }
                    _loadError.value = null
                }

                is Result.Error -> {
                    _categoryList.value = emptyList()
                    _loadError.value = result.exception.message
                }
            }

            _loading.value = false
        }
    }

    // Force refresh categories
    fun refreshCategories() {
        fetchCategories(forceReload = true)
    }
}
