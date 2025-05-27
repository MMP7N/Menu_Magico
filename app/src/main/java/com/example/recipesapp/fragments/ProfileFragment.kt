package com.example.recipesapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recipesapp.LogSig.LoginActivity
import com.example.recipesapp.R
import androidx.core.content.edit
import androidx.core.net.toUri
import com.example.app.DatabaseHelper

class ProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var preferencesEditText: EditText
    private lateinit var favoritesEditText: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var logoutButton: Button
    private lateinit var profileImageView: ImageView

    private lateinit var sharedPref: android.content.SharedPreferences
    private lateinit var dbHelper: DatabaseHelper

    private var currentProfileImageUri: Uri? = null
    private var currentProfileImageUrl: String? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                if (imageUri != null) {
                    currentProfileImageUri = imageUri
                    currentProfileImageUrl = null
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(profileImageView)

                    // Guardar URI temporal en SharedPreferences para mantenerla si se cambia imagen pero aún no se guarda DB
                    sharedPref.edit {
                        putString("profile_pic_uri_temp", imageUri.toString())
                        remove("profile_pic_temp")
                    }
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permiso denegado para acceder a la galería",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userNameTextView = view.findViewById(R.id.user_name)
        userEmailTextView = view.findViewById(R.id.user_email)
        preferencesEditText = view.findViewById(R.id.preferences_content)
        favoritesEditText = view.findViewById(R.id.favorites_content)
        saveChangesButton = view.findViewById(R.id.save_changes_button)
        logoutButton = view.findViewById(R.id.logout_button)
        profileImageView = view.findViewById(R.id.profile_image)

        sharedPref = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        dbHelper = DatabaseHelper(requireContext())

        val userName = sharedPref.getString("username", "Usuario") ?: "Usuario"
        val userEmail = sharedPref.getString("email", "usuario@email.com") ?: "usuario@email.com"

        userNameTextView.text = userName
        userEmailTextView.text = userEmail

        // Cargar datos guardados en BD para este usuario
        cargarDatosUsuario(userName)

        profileImageView.setOnClickListener {
            showImageSelectionDialog()
        }

        saveChangesButton.setOnClickListener {
            guardarCambiosUsuario(userName)
        }

        logoutButton.setOnClickListener {
            sharedPref.edit { clear() }
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    private fun cargarDatosUsuario(username: String) {
        val userDetails = dbHelper.getUserDetails(username)

        userNameTextView.text = userDetails["username"] ?: username
        userEmailTextView.text = userDetails["email"] ?: ""

        // Cargar preferencias y favoritos desde BD o SharedPreferences
        val prefsFromDB = userDetails["preferences"]
        val favsFromDB = userDetails["favorites"]

        preferencesEditText.setText(
            if (!prefsFromDB.isNullOrEmpty()) prefsFromDB
            else sharedPref.getString("preferences_$username", "")
        )

        favoritesEditText.setText(
            if (!favsFromDB.isNullOrEmpty()) favsFromDB
            else sharedPref.getString("favorites_$username", "")
        )

        val profilePicFromDB = userDetails["profile_pic"]
        if (!profilePicFromDB.isNullOrEmpty()) {
            currentProfileImageUrl = profilePicFromDB
            currentProfileImageUri = null
            Glide.with(this)
                .load(profilePicFromDB)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(profileImageView)
        } else {
            val tempUriString = sharedPref.getString("profile_pic_uri_temp", null)
            if (!tempUriString.isNullOrEmpty()) {
                currentProfileImageUri = tempUriString.toUri()
                Glide.with(this)
                    .load(currentProfileImageUri)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile)
            }
        }
    }

    private fun guardarCambiosUsuario(username: String) {
        val preferences = preferencesEditText.text.toString()
        val favorites = favoritesEditText.text.toString()

        // Guardar en SharedPreferences (opcional)
        sharedPref.edit {
            putString("preferences_$username", preferences)
            putString("favorites_$username", favorites)
        }

        val profilePicToSave = when {
            currentProfileImageUri != null -> currentProfileImageUri.toString()
            currentProfileImageUrl != null -> currentProfileImageUrl
            else -> ""
        } ?: ""

        val contentValues = android.content.ContentValues().apply {
            put("profile_pic", profilePicToSave)
            put("preferences", preferences)
            put("favorites", favorites)
        }

        val db = dbHelper.writableDatabase
        db.update("data", contentValues, "username = ?", arrayOf(username))

        Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
    }


    private fun showImageSelectionDialog() {
        val options = arrayOf("Elegir imagen de galería", "Introducir URL de imagen")
        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar imagen de perfil")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGalleryWithPermissionCheck()
                    1 -> showUrlInputDialog()
                }
            }
            .show()
    }

    private fun openGalleryWithPermissionCheck() {
        val permiso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permiso
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            shouldShowRequestPermissionRationale(permiso) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permiso requerido")
                    .setMessage("Necesitamos permiso para acceder a tus imágenes para seleccionar foto de perfil")
                    .setPositiveButton("Aceptar") { _, _ ->
                        requestPermissionLauncher.launch(permiso)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(permiso)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun showUrlInputDialog() {
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        input.hint = "https://ejemplo.com/miimagen.jpg"

        AlertDialog.Builder(requireContext())
            .setTitle("Introducir URL de imagen")
            .setView(input)
            .setPositiveButton("Aceptar") { _, _ ->
                val url = input.text.toString().trim()
                if (url.isNotEmpty()) {
                    currentProfileImageUrl = url
                    currentProfileImageUri = null
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(profileImageView)

                    sharedPref.edit {
                        putString("profile_pic_temp", url)
                        remove("profile_pic_uri_temp")
                    }
                } else {
                    Toast.makeText(requireContext(), "URL vacía", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
