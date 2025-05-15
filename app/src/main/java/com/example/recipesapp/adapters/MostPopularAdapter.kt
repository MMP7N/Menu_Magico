package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.PopularItemsBinding
import com.example.recipesapp.domain.model.MealsByCategory

// Adaptador para mostrar una lista horizontal de comidas más populares
class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    // Callback para manejar clics en los elementos de la lista
    lateinit var onItemClick: ((MealsByCategory) -> Unit)

    var onLongItemClick: ((MealsByCategory) -> Unit) ? = null

    // Lista de comidas más populares
    private var mealsList = ArrayList<MealsByCategory>()

    // Asigna una nueva lista de comidas al adaptador y actualiza la vista
    fun setMeals(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    // Crea e infla el ViewHolder para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Devuelve el número total de ítems en la lista
    override fun getItemCount(): Int {
        return mealsList.size
    }

    // Asocia los datos de la comida con las vistas del ViewHolder
    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItems)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }

    // ViewHolder que representa cada ítem de comida popular
    class PopularMealViewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
