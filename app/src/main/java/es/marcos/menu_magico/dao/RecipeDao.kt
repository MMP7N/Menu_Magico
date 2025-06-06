package es.marcos.menu_magico.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.marcos.menu_magico.entities.Recipes

@Dao
interface RecipeDao {

    @get:Query("SELECT * From recipes ORDER BY id DESC")
    val allRecipes: List<Recipes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipies: Recipes)

}