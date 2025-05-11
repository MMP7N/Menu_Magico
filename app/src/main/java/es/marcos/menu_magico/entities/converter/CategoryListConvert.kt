package es.marcos.menu_magico.entities.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.marcos.menu_magico.entities.Category

class CategoryListConvert {

    @TypeConverter
    fun fromCategoryList(category: List<Category>?): String? {
        if (category == null) {
            return null
        } else {
            val gson = Gson()
            val type = object : TypeToken<List<Category>>() {}.type
            return gson.toJson(category, type)
        }
    }

    @TypeConverter
    fun toCategoryList(categoryString: String?): List<Category>? {
        if (categoryString == null) {
            return null
        } else {
            val gson = Gson()
            val type = object : TypeToken<List<Category>>() {}.type
            return gson.fromJson(categoryString, type)
        }
    }
}
