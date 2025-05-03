package es.marcos.menu_magico

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_recipes -> selectedFragment = RecipesFragment() // Fragmento para Recetas
                R.id.nav_favorites -> selectedFragment = FavoritesFragment() // Fragmento para Recetas Favoritas
                R.id.nav_profile -> selectedFragment = ProfileFragment() // Fragmento para Perfil
            }

            // Reemplazar el fragmento actual con el seleccionado
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }

        // Configurar el fragmento inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecipesFragment()) // Inicialmente, mostrar el fragmento de Recetas
                .commit()
        }
    }
}
