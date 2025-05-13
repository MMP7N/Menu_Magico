package com.example.recipesapp.db

import android.content.Context
import androidx.room.*
import com.example.recipesapp.db.dao.MealDao
import com.example.recipesapp.pojo.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(com.example.recipesapp.db.converter.MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        var INSTANCE: MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java, "meal.db"
                ).build()
            }
            return INSTANCE as MealDatabase
        }
    }
}