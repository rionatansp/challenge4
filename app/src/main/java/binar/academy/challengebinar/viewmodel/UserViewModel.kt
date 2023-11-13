package binar.academy.challengebinar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import binar.academy.challengebinar.dao.UserDatabaseDao
import binar.academy.challengebinar.data.UserEntitiy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel (val userDao: UserDatabaseDao, application: Application) : AndroidViewModel(application) {
    private var _user: MutableLiveData<UserEntitiy> = MutableLiveData<UserEntitiy>()
    val user: LiveData<UserEntitiy> get() = _user

    val users: LiveData<List<UserEntitiy>> = userDao.getUsers()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                initializeUser()
            }
        }
    }

    fun setUserData() {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                getUserDatabase()
            }
            _user.value = user
        }
    }

    fun getUser(): UserEntitiy? {
        return user.value
    }

    fun inputUser(user: UserEntitiy) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setUserDatabase(user)
            }
        }
    }

    fun updateUser(user: UserEntitiy) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateUserDatabase(user)
            }
        }
    }

    fun clearUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                clearUserDatabase()
            }
        }
    }

    private suspend fun getUserDatabase(): UserEntitiy {
        return userDao.getUser()
    }

    private suspend fun setUserDatabase(user: UserEntitiy) {
        userDao.insert(user)
    }

    private suspend fun updateUserDatabase(user: UserEntitiy) {
        userDao.update(user)
    }

    private suspend fun clearUserDatabase() {
        userDao.clear()
    }

    private suspend fun initializeUser() {
        userDao.insert(UserEntitiy(username = "rionatan", password = "admin", email = "rionatansp@gmail.com", telephone = "085159545122"))
    }


}