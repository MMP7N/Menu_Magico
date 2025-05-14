package com.example.recipesapp.fragments

import android.content.Intent
import com.bumptech.glide.Glide
import com.example.recipesapp.pojo.Meal
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.activities.CategoryMealsActivity
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.activities.MealActivity
import com.example.recipesapp.adapters.CategoriesAdapter
import com.example.recipesapp.adapters.MostPopularAdapter
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.pojo.MealsByCategory
import com.example.recipesapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    // Constantes para pasar datos entre actividades
    companion object {
        const val MEAL_ID = "com.example.recipesapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.recipesapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recipesapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.recipesapp.fragments.categoryName"
    }

    // Se obtiene el ViewModel desde la actividad principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    // Se infla el layout del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configuración y observación de datos cuando la vista se ha creado
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepara el RecyclerView para mostrar las comidas populares
        preparePopularItemsRecyclerView()

        // Observa y obtiene una comida aleatoria
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        // Observa y obtiene las comidas populares
        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        // Prepara el RecyclerView para mostrar las categorías
        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
    }

    // Acción al hacer click sobre una categoría
    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            // Se pasa el nombre de la categoría a la actividad de comidas por categoría
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    // Configuración del RecyclerView para las categorías (en formato de cuadrícula)
    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()  // Inicializamos el adaptador para categorías
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter  // Asignamos el adaptador
        }
    }

    // Observa el LiveData de categorías para actualizar la lista
    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    // Acción al hacer click sobre una comida popular
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            // Se pasa información de la comida seleccionada a la actividad MealActivity
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    // Configuración del RecyclerView para las comidas populares (en formato horizontal)
    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    // Observa el LiveData de las comidas populares y actualiza la lista
    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    // Acción al hacer click sobre la comida aleatoria
    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            // Inicia la actividad MealActivity con los datos de la comida aleatoria
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    // Observa el LiveData de la comida aleatoria y la muestra en la interfaz
    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, { meal ->
            // Carga la imagen de la comida aleatoria usando Glide
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        })
    }
}
