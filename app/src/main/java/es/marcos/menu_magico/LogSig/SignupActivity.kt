package es.marcos.menu_magico.LogSig

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.marcos.menu_magico.databinding.ActivitySignupBinding

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
            val signupName = binding.signupName.text.toString()  // Nombre
            val signupProfilePic = "ruta_a_la_imagen"  // Puedes agregar la lógica para subir la imagen más tarde

            authViewModel.signup(signupUsername, signupPassword, signupEmail, signupName, signupProfilePic)
        }

        binding.loginRedirect.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}
