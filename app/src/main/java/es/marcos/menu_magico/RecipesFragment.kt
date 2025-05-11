package es.marcos.menu_magico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipesFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var rvMainCategory: RecyclerView
    private lateinit var rvSubCategory: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        // Referencias a las vistas del layout
        searchView = view.findViewById(R.id.search_view)
        rvMainCategory = view.findViewById(R.id.rv_main_category)
        rvSubCategory = view.findViewById(R.id.rv_sub_category)

        // Configurar los RecyclerViews con un LayoutManager
        rvMainCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvSubCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Listener para el SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Aquí puedes filtrar las recetas con la búsqueda
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Aquí puedes mostrar sugerencias o realizar filtrado dinámico
                return false
            }
        })

        return view
    }
}
