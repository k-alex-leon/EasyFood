package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Meal

class MealAdapter : RecyclerView.Adapter<MealAdapter.FavoriteMealAdapterViewHolder>() {

    // SI UN ELEMENTO ES ELIMINADO SOLO SE ACTUALIZA ESE ITEM EN CAMBIO A -
    // RECARGAR TODA LA VIEW

    lateinit var onMealClicked : ((Meal) -> Unit)

    private val mDiffUtil = object : DiffUtil.ItemCallback<Meal>(){
        // compara la key de los viejos datos con los nuevos
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            // devuelve true si los 2 id son iguales
            return oldItem.idMeal == newItem.idMeal
        }

        // compara el objeto completo
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            // devuelve true si los 2 objetos son iguales
            return oldItem == newItem
        }

    }

    val mDiffer = AsyncListDiffer(this, mDiffUtil)

    inner class FavoriteMealAdapterViewHolder(var binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealAdapterViewHolder {
        return FavoriteMealAdapterViewHolder(MealItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: FavoriteMealAdapterViewHolder, position: Int) {
        var meal = mDiffer.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgVMeal)

        holder.binding.txtVMeal.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onMealClicked.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }
}