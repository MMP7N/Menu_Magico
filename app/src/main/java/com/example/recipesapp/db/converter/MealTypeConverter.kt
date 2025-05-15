package com.example.recipesapp.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters

// Clase para convertir tipos de datos personalizados a tipos que Room puede guardar en la base de datos
@TypeConverters
class MealTypeConverter {

    // Convierte un atributo genérico (Any) a String antes de guardarlo en la base de datos
    @TypeConverter
    fun fromAnyToString(attribute: Any?): String {
        if (attribute == null)
            return ""
        return attribute as String
    }

    // Convierte un String desde la base de datos de vuelta a un tipo genérico (Any)
    @TypeConverter
    fun fromStringToAny(attribute: String?): Any {
        if (attribute == null)
            return ""
        return attribute
    }
}
