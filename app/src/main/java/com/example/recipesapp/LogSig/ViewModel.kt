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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val userExists = databaseHelper.readUser(username, password)
            loginResult.postValue(userExists)
        }
    }

    fun signup(username: String, password: String, email: String, profilePic: String) {
        viewModelScope.launch {
            val insertRowId = databaseHelper.insertUser(username, password, email, profilePic)
            signupResult.postValue(insertRowId)
        }
    }

    fun getUserDetails(username: String): Map<String, String> {
        return databaseHelper.getUserDetails(username)
    }
}
