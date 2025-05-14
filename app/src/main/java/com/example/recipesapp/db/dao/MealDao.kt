package com.example.recipesapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipesapp.pojo.Meal

// DAO (Data Access Object) para la entidad Meal.
// Define las operaciones que se pueden realizar en la base de datos relacionadas con las comidas.
@Dao
interface MealDao {

    // Inserta una nueva comida o actualiza si ya existe (por ID)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(meal: Meal)

    // Elimina una comida de la base de datos
    @Delete
    suspend fun delete(meal: Meal)

    // Obtiene todas las comidas almacenadas en la base de datos como LiveData
    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>
}
