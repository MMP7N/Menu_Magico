package com.example.recipesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.db.MealDatabase
import com.example.recipesapp.data.db.entity.Meal
import com.example.recipesapp.data.db.entity.MealList
import com.example.recipesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel encargado de manejar la lógica para obtener y manejar los detalles de una comida.
class MealViewModel(
    val mealDatabase: MealDatabase
) : ViewModel() {

    // LiveData que contendrá los detalles de una comida.
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    // Función para obtener los detalles de una comida desde la API usando Retrofit.
    fun getMealDetail(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            // En caso de error en la llamada a la API.
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("MealActivity", t.message.toString())
            }
        })
    }

    // Función que devuelve el LiveData con los detalles de la comida.
    fun observerMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }

    // Función para insertar o actualizar una comida en la base de datos.
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }
}
