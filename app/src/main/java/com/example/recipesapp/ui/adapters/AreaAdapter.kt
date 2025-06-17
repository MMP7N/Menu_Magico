package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.db.entity.Area

class AreaAdapter(
    private var areaList: List<Area>,
    private val onItemClick: (Area) -> Unit
) : RecyclerView.Adapter<AreaAdapter.AreaViewHolder>() {

    // Mapa de nombres de área a códigos ISO-3166-1 alpha-2
    private val isoMap = mapOf(
        "American" to "us",
        "British" to "gb",
        "Canadian" to "ca",
        "Chinese" to "cn",
        "Croatian" to "hr",
        "Dutch" to "nl",
        "Egyptian" to "eg",
        "Filipino" to "ph",
        "French" to "fr",
        "Greek" to "gr",
        "Indian" to "in",
        "Irish" to "ie",
        "Italian" to "it",
        "Jamaican" to "jm",
        "Japanese" to "jp",
        "Kenyan" to "ke",
        "Malaysian" to "my",
        "Mexican" to "mx",
        "Moroccan" to "ma",
        "Polish" to "pl",
        "Portuguese" to "pt",
        "Russian" to "ru",
        "Spanish" to "es",
        "Thai" to "th",
        "Tunisian" to "tn",
        "Turkish" to "tr",
        "Ukrainian" to "ua",
        "Uruguayan" to "uy",
        "Vietnamese" to "vn"
    )

    inner class AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArea: ImageView = itemView.findViewById(R.id.img_area)
        val tvAreaName: TextView = itemView.findViewById(R.id.tv_area_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.area_item, parent, false)
        return AreaViewHolder(view)
    }

    override fun getItemCount(): Int = areaList.size

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val area = areaList[position]
        holder.tvAreaName.text = area.strArea

        val code = isoMap[area.strArea]
        val flagUrl = code?.let {
            "https://flagcdn.com/80x60/$it.png"
        } ?: "https://flagcdn.com/80x60/un.png"

        Glide.with(holder.imgArea.context)
            .load(flagUrl)
            .placeholder(R.drawable.ic_area) // imagen local por defecto
            .into(holder.imgArea)

        holder.itemView.setOnClickListener { onItemClick(area) }
    }

    fun setData(newAreaList: List<Area>) {
        areaList = newAreaList
        notifyDataSetChanged()
    }
}
