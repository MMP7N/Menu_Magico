package com.example.recipesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.activities.CategoryMealsActivity
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.activities.MealActivity
import com.example.recipesapp.adapters.CategoriesAdapter
import com.example.recipesapp.adapters.MostPopularAdapter
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.data.db.entity.Meal
import com.example.recipesapp.data.db.entity.MealsByCategory
import com.example.recipesapp.fragments.bottomsheet.MealBottomSheetFragment
import com.example.recipesapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.recipesapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.recipesapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recipesapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.recipesapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar los RecyclerView
        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()

        // Obtener y observar datos
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()

        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onSeacrhIconClick()

        // Clic normal → abrir BottomSheet
        popularItemsAdapter.onItemClick = { meal ->
            val bottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            bottomSheetFragment.show(childFragmentManager, "Meal Info")
        }

        // Clic largo → también abrir BottomSheet (opcional)
        popularItemsAdapter.onLongItemClick = { meal ->
            val bottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            bottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onSeacrhIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoryList(categories)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }
}
