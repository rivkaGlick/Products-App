package com.example.productsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.productsapp.ui.categories.CategoriesScreen
import com.example.productsapp.ui.categories.CategoryUI
import com.example.productsapp.ui.details.CategoryDetailsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    var selectedCategory by remember { mutableStateOf<CategoryUI?>(null) }

    if (selectedCategory == null) {
        CategoriesScreen(
            onCategoryClick = { category ->
                selectedCategory = category
            }
        )
    } else {
        CategoryDetailsScreen(
            categoryName = selectedCategory!!.name,
            onBack = { selectedCategory = null }
        )
    }
}
