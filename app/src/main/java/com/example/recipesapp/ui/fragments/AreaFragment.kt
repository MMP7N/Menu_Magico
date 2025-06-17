package com.example.recipesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.AreaAdapter
import com.example.recipesapp.data.db.entity.Area
import com.example.recipesapp.viewModel.HomeViewModel

class AreaFragment : Fragment() {

    private lateinit var areaAdapter: AreaAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvArea = view.findViewById<RecyclerView>(R.id.rv_area)

        areaAdapter = AreaAdapter(emptyList()) { area -> onAreaClicked(area) }

        // LayoutManager tipo grid (2 columnas) para mostrar mejor las áreas
        rvArea.layoutManager = GridLayoutManager(requireContext(), 2)
        rvArea.adapter = areaAdapter

        // Observamos LiveData de áreas
        homeViewModel.observeAreasLiveData().observe(viewLifecycleOwner) { areas ->
            areaAdapter.setData(areas)
        }

        // Pedimos cargar las áreas
        homeViewModel.getAreas()
    }

    private fun onAreaClicked(area: Area) {
        // Aquí puedes manejar el clic en un área, por ejemplo:
        // Mostrar detalles, filtrar comidas por área, navegar a otro fragmento, etc.
        // Por ejemplo:
        println("Área clicada: ${area.strArea}")
    }
}
