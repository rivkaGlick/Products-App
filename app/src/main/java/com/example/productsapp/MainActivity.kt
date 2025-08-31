package com.example.productsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.productsapp.ui.categories.CategoriesScreen
import CategoryUI
import com.example.productsapp.ui.details.CategoryDetailsScreen

// Main entry point of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set Compose content for this activity
        setContent {
            App() // Call the root composable
        }
    }
}

// Root composable that handles navigation between screens
@Composable
fun App() {
    // Remember the currently selected category for navigation
    var selectedCategory by remember { mutableStateOf<CategoryUI?>(null) }

    // Show either CategoriesScreen or CategoryDetailsScreen based on selection
    selectedCategory?.let { category ->
        CategoryDetailsScreen(
            categoryName = category.name,
            onBack = { selectedCategory = null } // Navigate back to categories screen
        )
    } ?: CategoriesScreen(
        onCategoryClick = { category ->
            selectedCategory = category // Navigate to details screen when a category is clicked
        }
    )
}
