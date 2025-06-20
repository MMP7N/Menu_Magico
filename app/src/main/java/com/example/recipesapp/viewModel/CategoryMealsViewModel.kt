package com.example.recipesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.db.entity.MealsByCategory
import com.example.recipesapp.data.db.entity.MealsByCategoryList
import com.example.recipesapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// AuthViewModel que maneja la lógica para obtener las comidas por categoría
class CategoryMealsViewModel(): ViewModel() {

    // LiveData mutable que almacena la lista de comidas por categoría
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    // Función para obtener las comidas de una categoría específica
    fun getMealsByCategory(categoryName: String) {
        // Realiza la llamada a la API utilizando Retrofit para obtener las comidas de la categoría
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList> {
            // Si la respuesta es exitosa, se obtiene el cuerpo de la respuesta
            override fun onResponse(
                c: Call<MealsByCategoryList>,
                r: Response<MealsByCategoryList>
            ) {
                r.body()?.let { mealsList ->
                    // Se publica la lista de comidas obtenidas a la LiveData
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            // Si ocurre un error en la llamada a la API, se registra el error
            override fun onFailure(c: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())
            }
        })
    }

    // Método para observar los cambios en la lista de comidas
    fun observeMealsLiveData(): LiveData<List<MealsByCategory>> {
        return mealsLiveData
    }

    fun getMealsByArea(areaName: String) {
        RetrofitInstance.api.getMealsByArea(areaName).enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealsList ->
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())
            }
        })
    }
}