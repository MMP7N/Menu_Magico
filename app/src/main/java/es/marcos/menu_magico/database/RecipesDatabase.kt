package es.marcos.menu_magico.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.marcos.menu_magico.dao.RecipeDao
import es.marcos.menu_magico.entities.Recipies


@Database(entities = [Recipies::class], version = 1, exportSchema = false)
abstract class RecipesDatabase: RoomDatabase() {

    companion object{

        var recipesDatabase: RecipesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): RecipesDatabase{
            if (recipesDatabase != null){
                recipesDatabase = Room.databaseBuilder(
                    context,
                    RecipesDatabase::class.java,
                    "recipe.db"
                ).build()
            }
            return recipesDatabase!!
        }
    }
    abstract fun recipeDao(): RecipeDao

}