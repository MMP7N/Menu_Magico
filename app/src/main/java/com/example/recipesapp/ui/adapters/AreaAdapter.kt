package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.data.db.entity.Area

class AreaAdapter(
    private var areaList: List<Area>,
    private val onItemClick: (Area) -> Unit
) : RecyclerView.Adapter<AreaAdapter.AreaViewHolder>() {

    inner class AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArea: ImageView = itemView.findViewById(R.id.img_area)
        val tvAreaName: TextView = itemView.findViewById(R.id.tv_area_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.area_item, parent, false)
        return AreaViewHolder(view)
    }

    override fun getItemCount(): Int = areaList.size

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val area = areaList[position]
        holder.tvAreaName.text = area.strArea // Ajusta al campo correcto del modelo Area

        // Opcional: Si tienes imagen o quieres poner un icono, aquí puedes cargarlo, por ejemplo con Glide o Picasso
        // Glide.with(holder.imgArea.context).load(area.imageUrl).into(holder.imgArea)
        // Por ahora dejamos la imagen fija

        holder.itemView.setOnClickListener {
            onItemClick(area)
        }
    }

    // Para actualizar datos
    fun setData(newAreaList: List<Area>) {
        areaList = newAreaList
        notifyDataSetChanged()
    }
}
