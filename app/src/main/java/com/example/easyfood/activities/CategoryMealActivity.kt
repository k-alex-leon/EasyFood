package com.example.easyfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.R
import com.example.easyfood.adapters.MealDenominationAdapter
import com.example.easyfood.databinding.ActivityCategoryMealBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryMealBinding
    lateinit var mCategoryMvvm: CategoryMealsViewModel
    private lateinit var mCategoryName : String
    private lateinit var mMealDenominationAdapter: MealDenominationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mMealDenominationAdapter = MealDenominationAdapter()
        prepareMealDenominationRecyclerView()
        onMealDenominationClicked()

        mCategoryMvvm = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        mCategoryName = intent.getStringExtra("category_name").toString()
        mCategoryMvvm.getMealByCategory(mCategoryName)

        mCategoryMvvm.observeCategoryLiveData().observe(this, Observer { mealsList ->
            binding.txtVCategoryTitle.text = mealsList.size.toString()
            mMealDenominationAdapter.setMealDenomination(mealsList)
        })
    }

    private fun onMealDenominationClicked() {
        mMealDenominationAdapter.onMealDenominationClicked = { meal ->
            val intent = Intent(this@CategoryMealActivity, MealActivity::class.java)
            intent.putExtra("meal_id", meal.idMeal)
            intent.putExtra("meal_name", meal.strMeal)
            intent.putExtra("meal_thumb", meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareMealDenominationRecyclerView() {
        binding.rvMealsCategory.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mMealDenominationAdapter
        }
    }
}