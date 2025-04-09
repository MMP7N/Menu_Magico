package es.marcos.menu_magico.LogSig

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.marcos.menu_magico.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            val signupEmail =
                binding.signupEmail.text.toString()
            signupDatabase(signupUsername, signupPassword, signupEmail)
        }

        binding.loginRedirect.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun signupDatabase(username: String, password: String, email: String) {
        val insertRowId =
            databaseHelper.insertUser(username, password, email)
        if (insertRowId != -1L) {
            Toast.makeText(this, "Acceso permitido", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show()
        }
    }
}
