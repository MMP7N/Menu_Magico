package com.example.recipesapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.adapters.CategoryMealsAdapter
import com.example.recipesapp.databinding.ActivityCategoryMealsBinding
import com.example.recipesapp.fragments.HomeFragment
import com.example.recipesapp.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    // Inicializa la vista, configura RecyclerView y observa los datos de la categoría
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)
        val areaName = intent.getStringExtra("AREA_NAME")

        if (categoryName != null) {
            categoryMealsViewModel.getMealsByCategory(categoryName)
        }

        if (areaName != null) {
            categoryMealsViewModel.getMealsByArea(areaName)
        }

        categoryMealsViewModel.observeMealsLiveData().observe(this) { mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)
        }
    }


    // Configura el RecyclerView con un GridLayoutManager y el adaptador
    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }

        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)          // id del plato
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)       // nombre del plato
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb) // imagen del plato
            startActivity(intent)
        }
    }

}
