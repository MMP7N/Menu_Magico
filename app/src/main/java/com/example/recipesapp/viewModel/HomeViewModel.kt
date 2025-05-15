package com.example.recipesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.db.MealDatabase
import com.example.recipesapp.domain.model.Category
import com.example.recipesapp.domain.model.CategoryList
import com.example.recipesapp.domain.model.MealsByCategoryList
import com.example.recipesapp.domain.model.MealsByCategory
import com.example.recipesapp.domain.model.Meal
import com.example.recipesapp.domain.model.MealList
import com.example.recipesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel que maneja la lógica de la pantalla principal
class HomeViewModel(
    private val mealDatabase: MealDatabase // Dependencia de la base de datos local
) : ViewModel() {

    // LiveData para la comida aleatoria, elementos populares, categorías y comidas favoritas
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    // Función para obtener una comida aleatoria de la API
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                // Si la respuesta es exitosa, se obtiene la primera comida aleatoria y se actualiza el LiveData
                if (response.body() != null) {
                    val randomMeal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    Log.d("Test", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                } else {
                    println("Error: ${response.code()}")
                }
            }

            // Si ocurre un error en la solicitud, se muestra en el log
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    // Función para obtener los elementos populares de una categoría específica (por ejemplo, Seafood)
    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    // Si la respuesta es exitosa, se actualiza el LiveData con los elementos populares
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                // Si ocurre un error, se muestra en el log
                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }
            })
    }

    // Función para obtener las categorías de comidas desde la API
    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                // Si la respuesta es exitosa, se publica la lista de categorías en el LiveData
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            // Si ocurre un error, se muestra en el log
            override fun onFailure(c: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                // Si la respuesta es exitosa, se obtiene la comida por ID y se actualiza el LiveData
                    val meal = response.body()?.meals?.first()
                meal?.let{ meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            // Si ocurre un error en la solicitud, se muestra en el log
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    // Función para eliminar una comida de la base de datos local
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal) // Elimina la comida usando el DAO de la base de datos
        }
    }

    // Función para insertar o actualizar una comida en la base de datos local
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal) // Inserta o actualiza la comida usando el DAO de la base de datos
        }
    }

    // Métodos para observar los LiveData correspondientes
    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData(): LiveData<List<Meal>> {
        return favoriteMealsLiveData
    }
    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetMealLiveData
}
