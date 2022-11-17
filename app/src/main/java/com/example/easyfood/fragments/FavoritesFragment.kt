package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.MealAdapter
import com.example.easyfood.databinding.FragmentFavoritesBinding
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var binding : FragmentFavoritesBinding
    private lateinit var mViewModel : HomeViewModel
    private lateinit var mFavoritesMealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = (activity as MainActivity).mViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()
        onFavoriteMealClicked()
        // especificar el slidde del item
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // necesita obtener la pos del item
                val position = viewHolder.adapterPosition
                mViewModel.deleteMeal(mFavoritesMealAdapter.mDiffer.currentList[position])

                // al eliminar mostraremos un snack para devolver la data
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        mViewModel.insertMeal(mFavoritesMealAdapter.mDiffer.currentList[position])
                    }).show()

            }

        }
        // adjuntamos el touch-helper al recycler
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavoriteMeals)
    }

    private fun onFavoriteMealClicked() {
        mFavoritesMealAdapter.onMealClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("meal_id", meal.idMeal)
            intent.putExtra("meal_name", meal.strMeal)
            intent.putExtra("meal_thumb", meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        mFavoritesMealAdapter = MealAdapter()
        binding.rvFavoriteMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mFavoritesMealAdapter
        }
    }

    private fun observeFavorites() {
        mViewModel.observeFavoriteMealsLiveData().observe(viewLifecycleOwner, Observer { meals ->
            mFavoritesMealAdapter.mDiffer.submitList(meals)
        })
    }
}