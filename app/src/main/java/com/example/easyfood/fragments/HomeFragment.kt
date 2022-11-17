package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoryMealAdapter
import com.example.easyfood.adapters.PopularMealAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.bottomsheet.MealBottomSheetFragment
import com.example.easyfood.pojo.PopularMeal
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mViewModel : HomeViewModel
    private lateinit var mRandomMeal : Meal
    private lateinit var mPopularItemsAdapter : PopularMealAdapter
    private lateinit var mCategoryMealAdapter : CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // trae el viewModel desde el mainActivity
        mViewModel = (activity as MainActivity).mViewModel

        mPopularItemsAdapter = PopularMealAdapter()
        mCategoryMealAdapter = CategoryMealAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        mViewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClicked()

        mViewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClicked()
        onPopularItemLongClicked()

        prepareCategoriesRecyclerView()
        mViewModel.getCategories()
        observerCategoriesLiveData()
        onCategoryClicked()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgVSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onCategoryClicked() {
        mCategoryMealAdapter.onCategoryClick = { category ->
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra("category_name", category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = mCategoryMealAdapter
        }
    }

    private fun onPopularItemLongClicked() {
        mPopularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal detail")
        }
    }


    private fun onPopularItemClicked() {
        mPopularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("meal_id", meal.idMeal)
            intent.putExtra("meal_name", meal.strMeal)
            intent.putExtra("meal_thumb", meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mPopularItemsAdapter
        }
    }

    private fun onRandomMealClicked() {
        binding.cvRandomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("meal_id", mRandomMeal.idMeal)
            intent.putExtra("meal_name", mRandomMeal.strMeal)
            intent.putExtra("meal_thumb", mRandomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        mViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal>{
            override fun onChanged(meal: Meal?) {

                // glide == picasso / obtenemos la img del objeto randomMeal
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgVRandomMeal)

                mRandomMeal = meal
            }

        })
    }

    private fun observePopularItemsLiveData(){
        mViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            mPopularItemsAdapter.setMeals(mealList as ArrayList<PopularMeal>)
        }
    }

    private fun observerCategoriesLiveData() {
        mViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner){ categoriesList ->
            mCategoryMealAdapter.setCategoryList(categoriesList)
        }
    }
}