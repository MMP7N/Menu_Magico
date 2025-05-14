package com.example.recipesapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMealBinding
import com.example.recipesapp.db.MealDatabase
import com.example.recipesapp.fragments.HomeFragment
import com.example.recipesapp.pojo.Meal
import com.example.recipesapp.viewModel.MealViewModel
import com.example.recipesapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel

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

    private fun onFavoriteButtonClick() {
        binding.btnAddFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Plato guardado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal?> {
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal = t
                mealToSave = meal
                binding.tvCategory.text = "Categoria: ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal.strArea}"
                binding.tvInstructions.text = meal.strInstructions

                youtubeLink = meal.strYoutube.toString()

            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnAddFav.visibility = android.view.View.INVISIBLE
        binding.tvInstructionsTitle.visibility = android.view.View.INVISIBLE
        binding.tvCategory.visibility = android.view.View.INVISIBLE
        binding.tvArea.visibility = android.view.View.INVISIBLE
        binding.imgYoutube.visibility = android.view.View.INVISIBLE

    }

    private fun onResponseCase() {
        binding.progressBar.visibility = android.view.View.INVISIBLE
        binding.btnAddFav.visibility = android.view.View.VISIBLE
        binding.tvInstructionsTitle.visibility = android.view.View.VISIBLE
        binding.tvCategory.visibility = android.view.View.VISIBLE
        binding.tvArea.visibility = android.view.View.VISIBLE
        binding.imgYoutube.visibility = android.view.View.VISIBLE
    }
}
