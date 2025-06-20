package com.example.recipesapp.LogSig

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesapp.activities.MainActivity
import com.example.recipesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.loginResult.observe(this) { userExists ->
            if (userExists) {
                val username = binding.loginUsername.text.toString()
                val userDetails = authViewModel.getUserDetails(username)

                val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("username", userDetails["username"])
                    putString("email", userDetails["email"])
                    putString("profile_pic", userDetails["profile_pic"])
                    apply()
                }


                // Aquí agregamos el nombre y el correo del usuario (o los datos que quieras pasar)
                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                    putExtra("user_name", userDetails["username"])
                    putExtra("user_email", userDetails["email"])    // Cambia "email" por la clave correcta
                    putExtra("profile_pic", userDetails["profile_pic"])
                }

                Toast.makeText(this, "Welcome ${userDetails["username"]}", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Incorrect name or password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            authViewModel.login(username, password)
        }

        binding.signupRedirect.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }
}
