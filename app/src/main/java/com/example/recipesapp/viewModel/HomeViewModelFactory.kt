package com.example.recipesapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.db.MealDatabase

// Factory para crear instancias de HomeViewModel con una dependencia MealDatabase
class HomeViewModelFactory(
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {

    // Método que crea una nueva instancia del HomeViewModel y le pasa el MealDatabase como parámetro
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}
