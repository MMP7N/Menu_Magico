package com.example.recipesapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.db.MealDatabase

// Esta clase es un "factory" que se usa para crear una instancia de MealViewModel,
// ya que MealViewModel tiene un parámetro en su constructor (mealDatabase).
class MealViewModelFactory(
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {

    // Esta función se sobrecarga para crear la instancia del AuthViewModel con el parámetro necesario
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDatabase) as T
    }
}
