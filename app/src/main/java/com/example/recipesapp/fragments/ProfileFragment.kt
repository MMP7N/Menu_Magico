package com.example.recipesapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipesapp.LogSig.LoginActivity
import com.example.recipesapp.R

class ProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var preferencesEditText: EditText
    private lateinit var favoritesEditText: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inicializamos vistas
        userNameTextView = view.findViewById(R.id.user_name)
        userEmailTextView = view.findViewById(R.id.user_email)
        preferencesEditText = view.findViewById(R.id.preferences_content)
        favoritesEditText = view.findViewById(R.id.favorites_content)
        saveChangesButton = view.findViewById(R.id.save_changes_button)
        logoutButton = view.findViewById(R.id.logout_button)

        // Obtenemos datos de SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("username", "Usuario")
        val userEmail = sharedPref.getString("email", "usuario@email.com")

        // Mostramos datos en pantalla
        userNameTextView.text = userName
        userEmailTextView.text = userEmail

        // Botón de guardar cambios (puede guardar en SharedPreferences)
        saveChangesButton.setOnClickListener {
            val preferences = preferencesEditText.text.toString()
            val favorites = favoritesEditText.text.toString()
            sharedPref.edit().apply {
                putString("preferences", preferences)
                putString("favorites", favorites)
                apply()
            }
        }

        // Botón de cerrar sesión
        logoutButton.setOnClickListener {
            sharedPref.edit().clear().apply() // Limpiamos las preferencias
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mostrar datos guardados previamente si existen
        preferencesEditText.setText(sharedPref.getString("preferences", ""))
        favoritesEditText.setText(sharedPref.getString("favorites", ""))

        return view
    }
}
