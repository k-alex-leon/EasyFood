package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.CategoryMealActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoryMealAdapter
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mCategoryMealAdapter : CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = (activity as MainActivity).mViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerCategories()
        observeCategories()
        onCategoryClicked()
    }

    private fun onCategoryClicked() {
        mCategoryMealAdapter.onCategoryClick = { category ->
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra("category_name", category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        mViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { meals ->
            mCategoryMealAdapter.setCategoryList(meals)
        })
    }

    private fun prepareRecyclerCategories() {
        mCategoryMealAdapter = CategoryMealAdapter()
        binding.rvCategoriesF.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = mCategoryMealAdapter
        }
    }
}