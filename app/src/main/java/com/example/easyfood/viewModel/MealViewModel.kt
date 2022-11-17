package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.database.MealDatabase
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase) : ViewModel() {

    private var mMealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id : String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    mMealDetailLiveData.value = response.body()!!.meals[0]
                }else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealViewModel", t.message.toString())
            }

        })
    }

    fun observerMealDetailLiveData() : LiveData<Meal>{
        return mMealDetailLiveData
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsetMeal(meal)
        }
    }

}