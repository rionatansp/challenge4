package binar.academy.challengebinar.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import binar.academy.challengebinar.data.UserEntitiy

@Dao
interface UserDatabaseDao {
    @Insert
    fun insert(user: UserEntitiy)

    @Update
    fun update(user: UserEntitiy)

    @Delete
    fun delete(user: UserEntitiy)

    @Query("SELECT * FROM user_table")
    fun getUsers(): LiveData<List<UserEntitiy>>

    @Query("SELECT * FROM user_table ORDER BY user_id ASC LIMIT 1")
    fun getUser(): UserEntitiy

    @Query("DELETE FROM cart_table")
    fun clear()

}