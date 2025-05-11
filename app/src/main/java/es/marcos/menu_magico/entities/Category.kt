package es.marcos.menu_magico.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import es.marcos.menu_magico.entities.converter.CategoryListConvert

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "categoriesitems")
    @Expose
    @SerializedName("categories")
    @TypeConverters(CategoryListConvert::class)
    var categoriesitems: List<CategoryItems>? = null
)

