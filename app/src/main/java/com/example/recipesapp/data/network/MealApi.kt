package com.example.recipesapp.retrofit

import com.example.recipesapp.data.db.entity.AreaList
import com.example.recipesapp.data.db.entity.CategoryList
import com.example.recipesapp.data.db.entity.MealsByCategoryList
import com.example.recipesapp.data.db.entity.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz que define las llamadas a la API para obtener datos relacionados con las recetas
interface MealApi {

    // Llama a la API para obtener una comida aleatoria
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    // Llama a la API para obtener los detalles de una comida en particular, usando su ID
    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    // Llama a la API para obtener comidas populares filtradas por categoría
    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    // Llama a la API para obtener la lista de categorías de comidas
    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    // Llama a la API para obtener comidas filtradas por categoría
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealsByCategoryList>

    // Llama a la API para buscar comidas por nombre
    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String): Call<MealList>

    // Llama a la API para obtener la lista de áreas (áreas de comida)
    @GET("list.php?a=list")
    fun getAreas(): Call<AreaList>

}
