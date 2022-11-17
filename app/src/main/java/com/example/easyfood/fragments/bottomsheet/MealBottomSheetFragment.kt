package com.example.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mMealId : String? = null

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var mViewModel : HomeViewModel

    private var mMealName : String? = null
    private var mMealThumb : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMealId = it.getString(MEAL_ID)

        }

        mViewModel = (activity as MainActivity).mViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // si el id no es vacio ejecuta la fun
        mMealId?.let { mViewModel.getMealById(it) }

        observeBottomSheetMeal()
        onBottomSheetClick()
    }

    private fun onBottomSheetClick() {
        binding.bottomSheet.setOnClickListener {
            if (mMealName != null && mMealThumb != null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra("meal_id", mMealId)
                    putExtra("meal_name", mMealName)
                    putExtra("meal_Thumb", mMealThumb)
                }
                startActivity(intent)
            }
        }
    }

    // obtiene la info del item y la pasamos
    private fun observeBottomSheetMeal() {
        mViewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this)
                .load(meal.strMealThumb)
                .into(binding.imgVBottomSheep)

            binding.txtVBottomSheetLocation.text = meal.strArea
            binding.txtVBottomSheetCategory.text = meal.strCategory
            binding.txtVBottomSheetMealName.text = meal.strMeal

            mMealName = meal.strMeal
            mMealThumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}