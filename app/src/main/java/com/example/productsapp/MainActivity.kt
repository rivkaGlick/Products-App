package com.example.productsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.productsapp.ui.categories.CategoriesScreen
import com.example.productsapp.ui.details.CategoryDetailsScreen
import com.example.productsapp.CategoryUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsApp()
        }
    }
}

@Composable
fun ProductsApp() {
    // Keep track of the currently selected category
    var selectedCategory by remember { mutableStateOf<CategoryUI?>(null) }

    if (selectedCategory != null) {
        // Show details for the selected category
        CategoryDetailsScreen(
            categoryName = selectedCategory!!.name,
            onBackPressed = { selectedCategory = null } // Go back to the categories list
        )
    } else {
        // Show the list of categories
        CategoriesScreen(
            onCategorySelected = { category ->
                selectedCategory = category // Navigate to details screen when clicked
            }
        )
    }
}
