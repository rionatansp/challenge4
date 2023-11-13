package binar.academy.challengebinar.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import binar.academy.challengebinar.dao.MenuDatabaseDao
import java.lang.IllegalArgumentException

class MenuViewModelFactory (
    private val dataSource: MenuDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}
