package com.example.recipesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.activities.CategoryMealsActivity
import com.example.recipesapp.adapter.AreaAdapter
import com.example.recipesapp.data.db.entity.Area
import com.example.recipesapp.viewModel.HomeViewModel

class AreaFragment : Fragment() {

    private lateinit var areaAdapter: AreaAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var rvArea: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_area, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvArea = view.findViewById(R.id.rv_area)

        areaAdapter = AreaAdapter(emptyList()) { area -> onAreaClicked(area) }

        rvArea.layoutManager = GridLayoutManager(requireContext(), 3)
        rvArea.adapter = areaAdapter

        homeViewModel.observeAreasLiveData().observe(viewLifecycleOwner) { areas ->
            areaAdapter.setData(areas)
        }

        homeViewModel.getAreas()
    }

    private fun onAreaClicked(area: Area) {
        val intent = Intent(requireContext(), CategoryMealsActivity::class.java)
        intent.putExtra("AREA_NAME", area.strArea)
        startActivity(intent)
    }

}


