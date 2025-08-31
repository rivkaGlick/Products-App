package com.example.productsapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CategoryUI(
    val name: String,
    val thumbnail: String,
    val productsCount: Int,
    val totalStock: Int
)

class CategoriesViewModel : ViewModel() {

    private val repo = ProductsRepository()

    private val _categories = MutableStateFlow<List<CategoryUI>>(emptyList())
    val categories: StateFlow<List<CategoryUI>> get() = _categories

    fun loadCategories() {
        viewModelScope.launch {
            val products = repo.fetchProducts()
            val grouped = products.groupBy { it.category }
            val list = grouped.map { (category, items) ->
                CategoryUI(
                    name = category,
                    thumbnail = items.first().thumbnail,
                    productsCount = items.size,
                    totalStock = items.sumOf { it.stock }
                )
            }
            _categories.value = list
        }
    }
}
