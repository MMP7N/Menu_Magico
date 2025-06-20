package com.example.recipesapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.adapters.IngredientsAdapter
import com.example.recipesapp.databinding.ActivityMealBinding
import com.example.recipesapp.db.MealDatabase
import com.example.recipesapp.data.db.entity.Meal
import com.example.recipesapp.fragments.HomeFragment
import com.example.recipesapp.viewModel.MealViewModel
import com.example.recipesapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel
    private lateinit var ingredientsAdapter: IngredientsAdapter

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String

    private var mealToSave: Meal? = null

    // Inicializa la vista, el AuthViewModel y configura los elementos de la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteButtonClick()
    }

    // Obtiene los datos del intent (ID, nombre e imagen de la receta)
    private fun getInformationFromIntent() {
        intent?.let {
            mealId = it.getStringExtra(HomeFragment.MEAL_ID) ?: ""
            mealName = it.getStringExtra(HomeFragment.MEAL_NAME) ?: ""
            mealThumb = it.getStringExtra(HomeFragment.MEAL_THUMB) ?: ""
        }
    }

    // Establece la imagen y el título de la receta en la interfaz
    private fun setInformationInViews() {
        Glide.with(this)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(this, R.color.white)
        )
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(this, R.color.white)
        )
    }

    // Muestra el indicador de carga y oculta elementos hasta tener los datos
    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddFav.visibility = View.INVISIBLE
        binding.tvInstructionsTitle.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    // Muestra los elementos de la vista una vez cargados los datos
    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddFav.visibility = View.VISIBLE
        binding.tvInstructionsTitle.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

    // Observa los detalles de la receta y los muestra en la pantalla
    @SuppressLint("SetTextI18n")
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this) { meal ->
            onResponseCase()
            mealToSave = meal

            binding.tvArea.text = "Area: ${meal?.strArea ?: "N/A"}"
            binding.tvCategory.text = "Category: ${meal?.strCategory ?: "N/A"}"
            binding.tvInstructions.text = meal?.strInstructions ?: ""
            youtubeLink = meal?.strYoutube ?: ""

            val ingredients = meal?.let { getIngredientsList(it) } ?: emptyList()
            ingredientsAdapter = IngredientsAdapter(ingredients)
            binding.rvIngredients.layoutManager = GridLayoutManager(this, 3)
            binding.rvIngredients.adapter = ingredientsAdapter
        }
    }


    // Abre el enlace de YouTube al hacer clic en el ícono correspondiente
    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, youtubeLink.toUri())
            startActivity(intent)
        }
    }

    // Guarda la receta como favorita y muestra un mensaje
    private fun onFavoriteButtonClick() {
        binding.btnAddFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Saved recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Obtiene la lista de ingredientes y medidas de la receta utilizando reflexión
    private fun getIngredientsList(meal: Meal): List<IngredientsAdapter.Ingredient> {
        val list = mutableListOf<IngredientsAdapter.Ingredient>()
        val cls = meal::class.java

        for (i in 1..20) {
            val ingredientField = cls.getDeclaredField("strIngredient$i")
            val measureField = cls.getDeclaredField("strMeasure$i")

            ingredientField.isAccessible = true
            measureField.isAccessible = true

            val ingredient = ingredientField.get(meal) as? String
            val measure = measureField.get(meal) as? String

            if (!ingredient.isNullOrBlank() && !ingredient.equals("null", true)) {
                list.add(
                    IngredientsAdapter.Ingredient(
                        name = ingredient.trim(),
                        measure = measure?.trim() ?: ""
                    )
                )
            }
        }

        return list
    }
}
