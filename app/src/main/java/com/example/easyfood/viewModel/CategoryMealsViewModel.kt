package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.PopularList
import com.example.easyfood.pojo.PopularMeal
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel(){

    val mMealsLiveData = MutableLiveData<List<PopularMeal>>()

    fun getMealByCategory(categoryName : String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<PopularList>{
            override fun onResponse(call: Call<PopularList>, response: Response<PopularList>) {
                response.body()?.let { mealsList ->
                    mMealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<PopularList>, t: Throwable) {
                Log.d("CategoryMealsActivity", t.message.toString())
            }

        })
    }

    fun observeCategoryLiveData() : LiveData<List<PopularMeal>>{
        return mMealsLiveData
    }
}