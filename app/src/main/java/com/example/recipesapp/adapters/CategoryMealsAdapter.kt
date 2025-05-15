package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.MealItemBinding
import com.example.recipesapp.domain.model.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewMolder>() {

    private var mealsList = ArrayList<MealsByCategory>()

    // Establece la lista de comidas por categoría y notifica los cambios
    fun setMealsList(mealsList: List<MealsByCategory>) {
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    // ViewHolder para representar cada ítem de comida
    inner class CategoryMealsViewMolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Crea y devuelve un nuevo ViewHolder inflando el layout correspondiente
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewMolder {
        return CategoryMealsViewMolder(MealItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // Devuelve la cantidad total de elementos en la lista
    override fun getItemCount(): Int {
        return mealsList.size
    }

    // Asocia los datos de cada comida al ViewHolder correspondiente
    override fun onBindViewHolder(holder: CategoryMealsViewMolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = mealsList[position].strMeal
    }
}

