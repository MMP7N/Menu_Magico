package com.example.recipesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.pojo.Meal
import com.example.recipesapp.pojo.MealList
import retrofit2.Call
import retrofit2.Response
import com.example.recipesapp.retrofit.RetrofitInstance


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        RetrofitInstance.api.getRandomMeal().enqueue(object : retrofit2.Callback<MealList> {

            override fun onResponse(c: Call<MealList>, r: Response<MealList>) {
                if(r.body() != null) {
                    val randomMeal: Meal = r.body()!!.meals[0]
                    Glide.with(this@HomeFragment)
                        .load(randomMeal.strMealThumb)
                        .into(binding.imgRandomMeal)
                    Log.d("Test", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                }
                else {
                    return println("Error: ${r.code()}")
                }

            }

            override fun onFailure(c: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
}