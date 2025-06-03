package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.MealItemBinding
import com.example.recipesapp.domain.model.Meal

class MealsAdapter :
    RecyclerView.Adapter<MealsAdapter.FavoriteMealsAdapterViewHolder>() {

    // ViewHolder que mantiene la referencia al layout de cada ítem de comida favorita
    inner class FavoriteMealsAdapterViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // DiffUtil para detectar diferencias entre listas de comidas
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    // Maneja actualizaciones de la lista de forma eficiente en segundo plano
    val differ = AsyncListDiffer(this, diffUtil)

    // Listener para click en ítems
    private var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnMealClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        val binding =
            MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = FavoriteMealsAdapterViewHolder(binding)

        // Aquí ponemos el click listener para el itemView del ViewHolder
        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.invoke(differ.currentList[position])
            }
        }

        return holder
    }

    override fun onBindViewHolder(
        holder: FavoriteMealsAdapterViewHolder,
        position: Int
    ) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal
    }

    override fun getItemCount(): Int = differ.currentList.size
}
