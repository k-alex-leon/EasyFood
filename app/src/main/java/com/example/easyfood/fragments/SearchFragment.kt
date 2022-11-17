package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.MealAdapter
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var mViewModel : HomeViewModel
    private lateinit var mMealAdapter : MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = (activity as MainActivity).mViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerSearch()

        binding.imgVSearch.setOnClickListener {
            searchMeals()
        }

        observeSearchLiveData()
        onMealItemClicked()

        // busqueda en tiempo real
        var searchJob : Job? = null
        binding.etSearch.addTextChangedListener { searchQuery ->
            // cancelar el trabajo realizado antes de cambiar el txt
            searchJob?.cancel()
            // ejecutar nuevo trabajo al cambiar el txt
            searchJob = lifecycleScope.launch {
                // delay necesario para no ejecutar querys a cada rato
                delay(500)
                mViewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun onMealItemClicked() {
        mMealAdapter.onMealClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("meal_id", meal.idMeal)
            intent.putExtra("meal_name", meal.strMeal)
            intent.putExtra("meal_thumb", meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun searchMeals() {
        var searchQuery = binding.etSearch.text.toString()
        if (searchQuery.isNotEmpty()){
            mViewModel.searchMeals(searchQuery)
        }
    }

    private fun observeSearchLiveData() {
        mViewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
            mMealAdapter.mDiffer.submitList(mealsList)
        })
    }

    private fun prepareRecyclerSearch() {
        mMealAdapter = MealAdapter()
        binding.rvSearch.apply {
           layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
           adapter = mMealAdapter
        }
    }

}