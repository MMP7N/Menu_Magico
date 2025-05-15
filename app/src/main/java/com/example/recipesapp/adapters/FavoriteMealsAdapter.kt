package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.MealItemBinding
import com.example.recipesapp.domain.model.Meal

class FavoriteMealsAdapter :
    RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteMealsAdapterViewHolder>() {

    // ViewHolder que mantiene la referencia al layout de cada ítem de comida favorita
    inner class FavoriteMealsAdapterViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // DiffUtil para detectar diferencias entre listas de comidas
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        // Compara si dos ítems representan el mismo objeto
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        // Compara el contenido de dos ítems para detectar cambios
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    // Maneja actualizaciones de la lista de forma eficiente en segundo plano
    val differ = AsyncListDiffer(this, diffUtil)

    // Crea e infla el ViewHolder para un ítem de comida favorita
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        return FavoriteMealsAdapterViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // Asocia los datos de la comida con las vistas del ViewHolder
    override fun onBindViewHolder(
        holder: FavoriteMealsAdapterViewHolder,
        position: Int
    ) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal
    }

    // Devuelve el número total de ítems en la lista
    override fun getItemCount(): Int = differ.currentList.size
}
