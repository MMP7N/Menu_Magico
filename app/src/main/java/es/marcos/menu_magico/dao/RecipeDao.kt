package es.marcos.menu_magico.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.marcos.menu_magico.entities.Recipies

@Dao
interface RecipeDao {

    @get:Query("SELECT * From recipes ORDER BY id DESC")
    val allRecipes: List<Recipies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipies: Recipies)

}