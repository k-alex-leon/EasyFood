package com.example.easyfood.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.pojo.Meal

@Dao
interface MealDao {

    // si ya existe la data en la bd simplemente la remplaza
    // upsetMeal funciona como 2 fun en 1 / insert || update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsetMeal(meal : Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * from mealInformation")
    fun getAllMeals() : LiveData<List<Meal>>
}