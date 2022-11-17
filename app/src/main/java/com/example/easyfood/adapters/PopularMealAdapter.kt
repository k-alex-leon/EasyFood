package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemBinding
import com.example.easyfood.pojo.PopularMeal

class PopularMealAdapter() : RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {

    private var mMealList = ArrayList<PopularMeal>()
    lateinit var onItemClick: ((PopularMeal) -> Unit)

    var onLongItemClick : ((PopularMeal) -> Unit)? = null

    fun setMeals(mealList : ArrayList<PopularMeal>){
        mMealList = mealList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder(val binding : PopularItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mMealList[position].strMealThumb)
            .into(holder.binding.imgVPopularItem)

        holder.binding.txtVPopularItem.text = mMealList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mMealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mMealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mMealList.size
    }
}