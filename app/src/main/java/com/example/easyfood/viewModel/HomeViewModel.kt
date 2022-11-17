package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.database.MealDatabase
import com.example.easyfood.pojo.*
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private var mealDatabase: MealDatabase) : ViewModel() {

    private var mRandomMealLiveData = MutableLiveData<Meal>()
    private var mPopularItemsLiveData = MutableLiveData<List<PopularMeal>>()
    private var mCategoriesLiveData = MutableLiveData<List<Category>>()
    // para obtener meals guardados como favs
    private var mFavoriteMealListLiveData = mealDatabase.mealDao().getAllMeals()
    // obtener un unico meal para el detalle del onLongClick
    private var mBottomSheetMealLiveData = MutableLiveData<Meal>()
    // realizar busquedas
    private var mSearchMealsLiveData = MutableLiveData<List<Meal>>()


    // esto evitará hacer nuevas query cuando el celular gire
    private var saveRandomMeal : Meal? = null

    // obtiene el plato (comida random) de la api
    fun getRandomMeal(){

        saveRandomMeal?.let { randomMeal ->
            mRandomMealLiveData.postValue(randomMeal)
            return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            // se conecta al api
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                // obteniendo info del random-meal
                if (response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    mRandomMealLiveData.value = randomMeal
                    // guardamos temporalmente un meal
                    saveRandomMeal = randomMeal

                }else return

            }
            // falla la conexion
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    // obtener comidas (Category meals) populares
    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<PopularList>{
            override fun onResponse(call: Call<PopularList>, response: Response<PopularList>) {

                if (response.body() != null){
                    mPopularItemsLiveData.value = response.body()!!.meals
                }else return

            }

            override fun onFailure(call: Call<PopularList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                // es una forma de remplazar = " if(response.body != null) "
                response.body()?.let { categoryList ->
                    mCategoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    // obtener meal por id
    fun getMealById(id : String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {
                    mBottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    // utilizado para crear en db || devolver los fav eliminados
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsetMeal(meal)
        }
    }

    // eliminar data de los items fav guardados
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    // buscando meals especificos
    fun searchMeals(searchQuery : String) = RetrofitInstance.api.searchMeals(searchQuery)
        .enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    mSearchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })

    fun observeSearchMealsLiveData() : LiveData<List<Meal>> = mSearchMealsLiveData

    // mutableLiveData puede alterar su contenido $$ livedata solo puede leerlo
    // ayuda a escuchar el live-data
    fun observeRandomMealLiveData() : LiveData<Meal>{
        return mRandomMealLiveData
    }

    // otro observer encargado de los category items (popular items)
    fun observePopularItemsLiveData() : LiveData<List<PopularMeal>>{
        return mPopularItemsLiveData
    }

    fun observeCategoriesLiveData() : LiveData<List<Category>>{
        return mCategoriesLiveData
    }

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>>{
        return mFavoriteMealListLiveData
    }

    fun observeBottomSheetMeal() : LiveData<Meal>{
        return mBottomSheetMealLiveData
    }
}