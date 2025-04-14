package es.marcos.menu_magico.LogSig

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
    fun signup(username: String, password: String, email: String) {
        viewModelScope.launch {
            val insertRowId = databaseHelper.insertUser(username, password, email)
            signupResult.postValue(insertRowId)
        }
    }
}
