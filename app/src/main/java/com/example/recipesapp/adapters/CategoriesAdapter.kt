package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.CategoryItemBinding
import com.example.recipesapp.pojo.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoriesList = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)? = null

    // Establece la lista de categorías y notifica el cambio
    fun setCategoryList(categoriesList: List<Category>) {
        this.categoriesList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    // ViewHolder que representa cada ítem de categoría
    inner class CategoryViewHolder(binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Infla el layout del ítem de categoría y crea el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // Devuelve el número de elementos en la lista
    override fun getItemCount(): Int {
        return categoriesList.size
    }

    // Asocia los datos con cada ítem de la lista y gestiona el click
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoriesList[position].strCategoryThumb)
            .into(holder.itemView.findViewById<ImageView>(R.id.img_category))

        holder.itemView.findViewById<TextView>(R.id.tv_category_name).text =
            categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(categoriesList[position])
        }
    }
}
