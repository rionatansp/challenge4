package binar.academy.challengebinar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.utils.InitialDataUtil
import binar.academy.challengebinar.dao.MenuDatabaseDao
import binar.academy.challengebinar.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuViewModel (val database: MenuDatabaseDao, application: Application) : AndroidViewModel(application) {
   // Live Data untuk Database ROOM
   val menuList: LiveData<List<MenuItemEntity>> = database.getAllMenu()
    val menuMakanan: LiveData<List<MenuItemEntity>> = liveData(viewModelScope.coroutineContext) {
        emitSource(database.getMenuByCategory("makanan"))
    }
    val menuMinuman: LiveData<List<MenuItemEntity>> = liveData(viewModelScope.coroutineContext) {
        emitSource(database.getMenuByCategory("minuman"))
    }
    val menuRekomendasi: LiveData<List<MenuItemEntity>> = liveData(viewModelScope.coroutineContext) {
        emitSource(database.getMenuRekomendasi())
    }

    // Live Data hanya untuk mempertahankan data di Detail Fragment
    private var _menuDetail: MutableLiveData<MenuItemEntity> = MutableLiveData<MenuItemEntity>()
    val menuDetail: LiveData<MenuItemEntity> get() = _menuDetail


    // Fungsi dalam menuDetail
    fun setMenuDetail(menuItem: MenuItemEntity) {
        _menuDetail.value = menuItem
    }

    fun getDataMenuDetail(): LiveData<MenuItemEntity> {
        return menuDetail
    }



    // CREATE
    fun insertMenu(menuItem: MenuItemEntity) {
        viewModelScope.launch {
            insertMenuDatabase(menuItem)

        }
    }

    // READ DATA MAKANAN
//    fun getMenuMakanan(): List<MenuItemEntity> {
//        var makanan: List<MenuItemEntity> = listOf()
//        viewModelScope.launch {
//           makanan = getMenuMakananDatabase("makanan")
//        }
//        return makanan
//    }
//
//    fun getMenuMinuman(): List<MenuItemEntity> {
//        var minuman: List<MenuItemEntity> = listOf()
//        viewModelScope.launch {
//            minuman = getMenuMakananDatabase("minuman")
//        }
//        return minuman
//    }

//    fun getMenuRekomendasi(): List<MenuItemEntity> {
//        var rekomendasi: List<MenuItemEntity> = listOf()
//        viewModelScope.launch {
//            rekomendasi = getMenuRekomendasiDatabase()
//        }
//        return rekomendasi
//    }


    // DELETE
    fun deleteMenu(menuItem: MenuItemEntity) {
        viewModelScope.launch {
            deleteMenuDatabase(menuItem)
        }
    }

    fun deleteAllMenu() {
        viewModelScope.launch {
            deleteAllMenuDatabse()
        }
    }

    // UPDATE

    fun updateMenu(menuItem: MenuItemEntity) {
        viewModelScope.launch {
            updateMenuDatabase(menuItem)
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!SharedPreferencesManager.isMenuInitialized) {
                SharedPreferencesManager.isMenuInitialized = true
                initializeDatabase()
            }
        }
    }

    // Inisialisasi Database
    private suspend fun initializeDatabase() {
        database.insertInitial(InitialDataUtil.getInitialDataMenu())
    }


    // Fungsi Private sebagai jembatan database, menerapkan Coroutine //

    private suspend fun getAllMenuDatabase(): LiveData<List<MenuItemEntity>> {
        return database.getAllMenu()
    }

//    private suspend fun getMenuMakananDatabase(category: String): List<MenuItemEntity> {
//        return database.getMenuByCategory(category)
//    }
//
//    private suspend fun getMenuRekomendasiDatabase(): List<MenuItemEntity> {
//        return database.getMenuRekomendasi()
//    }


    private suspend fun insertMenuDatabase(menuItem: MenuItemEntity) {
        database.insert(menuItem)
    }

    private suspend fun deleteAllMenuDatabse() {
        database.deleteAllMenu()
    }

    private suspend fun deleteMenuDatabase(menuItem: MenuItemEntity) {
        database.delete(menuItem)
    }

    private suspend fun updateMenuDatabase(menuItem: MenuItemEntity) {
        database.update(menuItem)
    }

}
