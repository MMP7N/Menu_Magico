package com.example.recipesapp.retrofit

import com.example.recipesapp.pojo.CategoryList
import com.example.recipesapp.pojo.MealsByCategoryList
import com.example.recipesapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Llamada a la parte de la API que queremos
interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>
}
