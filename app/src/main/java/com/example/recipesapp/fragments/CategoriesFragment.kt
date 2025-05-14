package com.example.recipesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.adapters.CategoriesAdapter
import com.example.recipesapp.databinding.FragmentCategoriesBinding
import com.example.recipesapp.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se obtiene el ViewModel desde la actividad principal
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout asociado con el Fragmento
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Preparar el RecyclerView y configurar el observador
        prepareRecyclerView()
        observeCategories()
    }

    // Método que observa los datos de las categorías del ViewModel
    private fun observeCategories() {
        // Observa los cambios en la lista de categorías y actualiza el adaptador
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoryList(categories) // Actualiza el adaptador con los nuevos datos
        }
    }

    // Configuración del RecyclerView
    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            // Configura el LayoutManager para mostrar las categorías en una cuadrícula de 3 columnas
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            // Asigna el adaptador al RecyclerView
            adapter = categoriesAdapter
        }
    }
}
