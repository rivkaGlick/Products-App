package com.example.productsapp.ui.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.productsapp.CategoryUI
import com.example.productsapp.CategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onCategorySelected: (CategoryUI) -> Unit,
    viewModel: CategoriesViewModel = viewModel()
) {
    // Collect UI state from ViewModel
    val categoryList by viewModel.categoryList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.loadError.collectAsState()

    // Load categories when screen first appears
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                actions = {
                    IconButton(onClick = { viewModel.refreshCategories() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh categories")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    errorMessage != null -> {
                        Text(
                            text = "Error: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(categoryList) { category ->
                                CategoryItemCard(
                                    category = category,
                                    onClick = { onCategorySelected(category) }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItemCard(category: CategoryUI, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(category.thumbnail),
                contentDescription = "Category image",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(category.name, fontWeight = FontWeight.Bold)
                Text("Products: ${category.itemCount}")
                Text("Stock: ${category.totalStock}")
            }
        }
    }
}
