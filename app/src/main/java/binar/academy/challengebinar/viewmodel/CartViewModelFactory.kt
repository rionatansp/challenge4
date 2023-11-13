package binar.academy.challengebinar.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import binar.academy.challengebinar.dao.CartDatabaseDao
import binar.academy.challengebinar.dao.CartDetailDatabaseDao
import java.lang.IllegalArgumentException

class CartViewModelFactory (
    private val cartDao: CartDatabaseDao,
    private val cartDetailDao: CartDetailDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                return CartViewModel(cartDao, cartDetailDao, application) as T
            }
            throw IllegalArgumentException("Uknown ViewModel class")
        }
}