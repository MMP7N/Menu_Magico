package com.example.recipesapp.LogSig

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observando el resultado del login
        authViewModel.loginResult.observe(this, { userExists ->
            if (userExists) {
                val username = binding.loginUsername.text.toString()

                // Obtener detalles del usuario (nombre y foto)
                val userDetails = authViewModel.getUserDetails(username)

                // Mostrar los detalles en MainActivity
                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                    putExtra("user_name", userDetails["name"])
                    putExtra("profile_pic", userDetails["profile_pic"])
                }
                Toast.makeText(this, "Bienvenido ${userDetails["name"]}", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Nombre o contraseña incorrecto", Toast.LENGTH_SHORT).show()
            }
        })

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            authViewModel.login(username, password)
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
