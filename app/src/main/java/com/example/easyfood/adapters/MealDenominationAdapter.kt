package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.PopularMeal

class MealDenominationAdapter() : RecyclerView.Adapter<MealDenominationAdapter.MealDenominationViewHolder>(){

    private var mMealDenomination = ArrayList<PopularMeal>()
    lateinit var onMealDenominationClicked : ((PopularMeal) -> Unit)

    fun setMealDenomination(mealCategory : List<PopularMeal>){
        mMealDenomination = mealCategory as ArrayList
        notifyDataSetChanged()
    }

    class MealDenominationViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealDenominationViewHolder {
        return MealDenominationViewHolder(MealItemBinding
            .inflate(LayoutInflater.from(parent.context)
                , parent, false))

    }

    override fun onBindViewHolder(holder: MealDenominationViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mMealDenomination[position].strMealThumb)
            .into(holder.binding.imgVMeal)

        holder.binding.txtVMeal.text = mMealDenomination[position].strMeal

        holder.itemView.setOnClickListener {
            onMealDenominationClicked.invoke(mMealDenomination[position])
        }
    }

    override fun getItemCount(): Int {
        return mMealDenomination.size
    }
}