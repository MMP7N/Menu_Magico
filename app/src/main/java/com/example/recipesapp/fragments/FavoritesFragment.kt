package com.example.recipesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.adapters.FavoriteMealsAdapter
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: FavoriteMealsAdapter

    // Se obtiene el ViewModel de la actividad principal para compartir datos entre fragmentos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    // Inflamos el layout del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Inicializamos la vista y observamos los datos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavorites()  // Observa los cambios en las comidas favoritas
        prepareRecyclerView()  // Configura el RecyclerView
    }

    // Observamos el LiveData de comidas favoritas
    private fun observeFavorites() {
        viewModel.observeFavoriteMealsLiveData().observe(viewLifecycleOwner) { meals ->
            favoritesAdapter.differ.submitList(meals)  // Actualiza la lista de comidas en el adaptador
            meals.forEach {
                Log.d("test", "Meal: ${it.strMeal}")  // (Depuración) Log de las comidas
            }
        }
    }

    // Configura el RecyclerView para mostrar los elementos en una cuadrícula
    private fun prepareRecyclerView() {
        favoritesAdapter = FavoriteMealsAdapter()  // Instanciamos el adaptador
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)  // Configura el layout
            adapter = favoritesAdapter  // Asignamos el adaptador
        }

        // Configura el ItemTouchHelper para permitir eliminar elementos con swipe
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true  // No hacemos nada en el movimiento

            // Lógica para manejar el swipe (deslizar) y eliminar un elemento
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList[position]

                viewModel.deleteMeal(deletedMeal)  // Eliminamos la comida

                // Mostramos un Snackbar para permitir deshacer la eliminación
                Snackbar.make(
                    requireView(),
                    "Plato eliminado",
                    Snackbar.LENGTH_LONG
                ).setAction("Deshacer") {
                    viewModel.insertMeal(deletedMeal)  // Restauramos la comida eliminada si el usuario desea deshacer
                }.show()
            }
        }

        // Asignamos el ItemTouchHelper al RecyclerView
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }
}
