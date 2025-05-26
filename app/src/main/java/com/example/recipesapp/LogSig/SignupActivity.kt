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

        authViewModel.signupResult.observe(this) { insertRowId ->
            if (insertRowId != -1L) {
                Toast.makeText(this, "Acceso permitido", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupButton.setOnClickListener {
            val username = binding.signupUsername.text.toString()
            val password = binding.signupPassword.text.toString()
            val email = binding.signupEmail.text.toString()
            val profilePic = "https://tse2.mm.bing.net/th/id/OIP.2cOe4ej-roywKGJXcvQQUQHaF7?rs=1&pid=ImgDetMain"
            authViewModel.signup(username, password, email, profilePic)
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
