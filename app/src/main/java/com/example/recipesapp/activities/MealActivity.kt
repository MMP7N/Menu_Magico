package com.example.recipesapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMealBinding
import com.example.recipesapp.fragments.HomeFragment
import com.example.recipesapp.pojo.Meal
import com.example.recipesapp.viewModel.MealViewModel

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

        mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)

        getInformationFromIntent()

        setInformationInViews()

        loadingCase()

        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal?> {
            override fun onChanged(meal: Meal?) {
                onResponseCase()
                meal?.let {
                    binding.tvCategory.text = "Categoria: ${it.strCategory}"
                    binding.tvArea.text = "Area: ${it.strArea}"
                    binding.tvInstructions.text = it.strInstructions

                    youtubeLink = it.strYoutube!!
                }
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
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) !!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) !!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB) !!
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
