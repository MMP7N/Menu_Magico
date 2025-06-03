package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R

class IngredientsAdapter(
    private val ingredients: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    data class Ingredient(
        val name: String,
        val measure: String
    )

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewIngredient: ImageView = itemView.findViewById(R.id.imageViewIngredient)
        val textViewIngredientName: TextView = itemView.findViewById(R.id.textViewIngredientName)
        val textViewIngredientMeasure: TextView = itemView.findViewById(R.id.textViewIngredientMeasure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.textViewIngredientName.text = ingredient.name
        holder.textViewIngredientMeasure.text = ingredient.measure

        val imageUrl = "https://www.themealdb.com/images/ingredients/${ingredient.name}.png"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.color.accent)
            .error(R.drawable.ic_placeholder)
            .into(holder.imageViewIngredient)
    }

    override fun getItemCount(): Int = ingredients.size
}
