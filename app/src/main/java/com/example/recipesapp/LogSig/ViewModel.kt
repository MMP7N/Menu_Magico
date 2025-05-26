package com.example.recipesapp.LogSig

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseHelper = DatabaseHelper(application)
    val loginResult = MutableLiveData<Boolean>()
    val signupResult = MutableLiveData<Long>()

    // Método para iniciar sesión
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val userExists = databaseHelper.readUser(username, password)
            loginResult.postValue(userExists)
        }
    }

    // Método para registrarse
    fun signup(username: String, password: String, email: String,  profilePic: String) {
        viewModelScope.launch {
            val insertRowId = databaseHelper.insertUser(username, password, email, profilePic)
            signupResult.postValue(insertRowId)
        }
    }

    // Método para obtener los detalles del usuario (nombre y foto de perfil)
    fun getUserDetails(username: String): Map<String, String> {
        return databaseHelper.getUserDetails(username)
    }
}
