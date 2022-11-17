package com.example.easyfood.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attr : Any?) : String{
        if (attr == null) return ""
        else return attr as String
    }

    @TypeConverter
    fun fromStringToAny(attr : String?) : Any{
        if (attr == null) return ""
        else return attr
    }
}