package binar.academy.challengebinar.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import binar.academy.challengebinar.dao.MenuDatabaseDao
import binar.academy.challengebinar.dao.UserDatabaseDao
import binar.academy.challengebinar.viewmodels.MenuViewModel
import java.lang.IllegalArgumentException

class UserViewModelFactory (
    private val userDao: UserDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userDao, application) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}