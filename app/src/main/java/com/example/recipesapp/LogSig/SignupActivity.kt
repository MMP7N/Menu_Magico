package com.example.recipesapp.LogSig

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val authViewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observando el resultado del signup
        authViewModel.signupResult.observe(this, { insertRowId ->
            if (insertRowId != -1L) {
                Toast.makeText(this, "Acceso permitido", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show()
            }
        })

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            val signupEmail = binding.signupEmail.text.toString()
            val signupProfilePic = "https://tse2.mm.bing.net/th/id/OIP.2cOe4ej-roywKGJXcvQQUQHaF7?rs=1&pid=ImgDetMain"

            authViewModel.signup(signupUsername, signupPassword, signupEmail, signupProfilePic)
        }

        binding.loginRedirect.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}
