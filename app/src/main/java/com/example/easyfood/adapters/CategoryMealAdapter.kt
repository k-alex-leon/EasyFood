package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.pojo.Category

class CategoryMealAdapter() : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {

    private var mCategoryMeal = ArrayList<Category>()
    lateinit var onCategoryClick : ((Category) -> Unit)

    fun setCategoryList(categoryList : List<Category>){
        mCategoryMeal = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    class CategoryMealViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mCategoryMeal[position].strCategoryThumb)
            .into(holder.binding.imgVCategory)

        holder.binding.txtVCategory.text = mCategoryMeal[position].strCategory

        holder.itemView.setOnClickListener {
            onCategoryClick.invoke(mCategoryMeal[position])
        }
    }

    override fun getItemCount(): Int {
        return mCategoryMeal.size
    }
}