package com.example.recipesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.pojo.Meal
import com.example.recipesapp.pojo.MealList
import com.example.recipesapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Response

class HomeViewModel() : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : retrofit2.Callback<MealList> {

            override fun onResponse(c: Call<MealList>, r: Response<MealList>) {
                if (r.body() != null) {
                    val randomMeal: Meal = r.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    Log.d("Test", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                } else {
                    println("Error: ${r.code()}")
                }
            }

            override fun onFailure(c: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }
}
