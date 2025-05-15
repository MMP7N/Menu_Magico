package com.example.recipesapp.db

import android.content.Context
import androidx.room.*
import com.example.recipesapp.db.dao.MealDao
import com.example.recipesapp.domain.model.Meal

// Esta clase es la base de datos que usa Room para almacenar datos relacionados con las comidas.
// Define la base de datos y proporciona el acceso a las operaciones de la base de datos (DAO).
@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(com.example.recipesapp.db.converter.MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {

    // Método abstracto que devuelve el DAO (Data Access Object) para la entidad Meal.
    abstract fun mealDao(): MealDao

    // Compañero para manejar la instancia de la base de datos, siguiendo el patrón Singleton.
    companion object {
        private var INSTANCE: MealDatabase? = null

        // Método que garantiza que solo haya una instancia de la base de datos (patrón Singleton)
        @Synchronized
        fun getInstance(context: Context): MealDatabase {
            if (INSTANCE == null) {
                // Crea la base de datos si no existe
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java, "meal.db" // Nombre del archivo de base de datos
                ).build()
            }
            return INSTANCE as MealDatabase
        }
    }
}
