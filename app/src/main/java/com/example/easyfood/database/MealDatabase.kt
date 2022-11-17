package com.example.easyfood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyfood.pojo.Meal

// en caso de cambiar algo en las entidades cambiar la version
@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao() : MealDao

    companion object{
        var INSTANCE : MealDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : MealDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE as MealDatabase
        }
    }
}