package com.example.recipesapp.retrofit

import com.example.recipesapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

//Llamada a la parte de la API que queremos
interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}
