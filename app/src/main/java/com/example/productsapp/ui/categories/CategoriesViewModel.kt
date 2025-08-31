import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.repository.ProductsRepository
import com.example.productsapp.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// CategoryUI - model representing a product category in the first screen
data class CategoryUI(
    val name: String,
    val thumbnail: String,
    val productsCount: Int,
    val totalStock: Int
)

// ViewModel to manage category data
class CategoriesViewModel : ViewModel() {

    private val repo = ProductsRepository() // Responsible for fetching product data from the API

    // StateFlow holding the list of categories for UI
    private val _categories = MutableStateFlow<List<CategoryUI>>(emptyList())
    val categories: StateFlow<List<CategoryUI>> get() = _categories

    // StateFlow to track errors
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // StateFlow to track loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Function to load categories from the server
    fun loadCategories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            when (val result = repo.fetchProducts(forceRefresh)) {
                is Result.Success -> {
                    val grouped = result.data.groupBy { it.category }
                    _categories.value = grouped.map { (category, items) ->
                        CategoryUI(
                            name = category,
                            thumbnail = items.first().thumbnail, // Thumbnail of the first product in the category
                            productsCount = items.size,
                            totalStock = items.sumOf { it.stock }
                        )
                    }
                    _error.value = null
                }
                is Result.Error -> {
                    _categories.value = emptyList()
                    _error.value = result.exception.message
                }
            }
            _isLoading.value = false
        }
    }

    fun refreshCategories() {
        loadCategories(forceRefresh = true)
    }
}
