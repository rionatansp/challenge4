package binar.academy.challengebinar.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import binar.academy.challengebinar.data.MenuItemEntity

@Dao
interface MenuDatabaseDao {
    @Insert
    fun insert(menuItem: MenuItemEntity)

    @Insert
    fun insertInitial(menuItem: List<MenuItemEntity>)

    @Update
    fun update(menuItem: MenuItemEntity)

    @Delete
    fun delete(menuItem: MenuItemEntity)

    @Query("SELECT * FROM menu_table ORDER BY menu_id ASC")
    fun getAllMenu(): LiveData<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_table WHERE menu_category = :category ORDER BY menu_id ASC")
    fun getMenuByCategory(category: String): LiveData<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_table WHERE menu_isrecommendation = 1 ORDER BY menu_id ASC")
    fun getMenuRekomendasi(): LiveData<List<MenuItemEntity>>

    @Query("DELETE FROM menu_table")
    fun deleteAllMenu()


}