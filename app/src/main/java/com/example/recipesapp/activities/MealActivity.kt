package com.example.recipesapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMealBinding
import com.example.recipesapp.db.MealDatabase
import com.example.recipesapp.fragments.HomeFragment
import com.example.recipesapp.domain.model.Meal
import com.example.recipesapp.viewModel.MealViewModel
import com.example.recipesapp.viewModel.MealViewModelFactory
import androidx.core.net.toUri

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel

    // Configura la vista, obtiene los datos y define los listeners
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

    // Maneja el click para agregar la comida a favoritos
    private fun onFavoriteButtonClick() {
        binding.btnAddFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Plato guardado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Maneja el click para abrir el enlace de YouTube
    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, youtubeLink.toUri())
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null

    // Observa los datos de la comida y actualiza la UI
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this
        ) { t ->
            onResponseCase()
            val meal = t
            mealToSave = meal
            binding.tvCategory.text = "Categoria: ${meal!!.strCategory}"
            binding.tvArea.text = "Area: ${meal.strArea}"
            binding.tvInstructions.text = meal.strInstructions

            youtubeLink = meal.strYoutube.toString()
        }
    }

    // Muestra la información básica en la vista
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    // Extrae la información del intent recibido
    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    // Muestra elementos de carga mientras llegan los datos
    private fun loadingCase() {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnAddFav.visibility = android.view.View.INVISIBLE
        binding.tvInstructionsTitle.visibility = android.view.View.INVISIBLE
        binding.tvCategory.visibility = android.view.View.INVISIBLE
        binding.tvArea.visibility = android.view.View.INVISIBLE
        binding.imgYoutube.visibility = android.view.View.INVISIBLE
    }

    // Muestra los elementos de la UI cuando se obtienen los datos
    private fun onResponseCase() {
        binding.progressBar.visibility = android.view.View.INVISIBLE
        binding.btnAddFav.visibility = android.view.View.VISIBLE
        binding.tvInstructionsTitle.visibility = android.view.View.VISIBLE
        binding.tvCategory.visibility = android.view.View.VISIBLE
        binding.tvArea.visibility = android.view.View.VISIBLE
        binding.imgYoutube.visibility = android.view.View.VISIBLE
    }
}
