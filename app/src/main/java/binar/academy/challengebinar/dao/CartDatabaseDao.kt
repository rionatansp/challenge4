package binar.academy.challengebinar.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.utils.Pembayaran
import binar.academy.challengebinar.utils.Pengiriman
import binar.academy.challengebinar.utils.Status

@Dao
interface CartDatabaseDao {
    @Insert
    fun insert(cart: CartEntity)

    @Update
    fun update(cart: CartEntity)

    @Delete
    fun delete(cart: CartEntity)

//    @Query("UPDATE cart_table SET total_price = :totalPrice WHERE cart_id = :cartOwnerId")
//    fun updateCartPembayaran(cartId: Int, )

    @Query("UPDATE cart_table SET status = :status WHERE cart_id = :cartOwnerId")
    fun updateStatus(cartOwnerId: Int, status: Status)

    @Query("UPDATE cart_table SET metode_pembayaran = :pembayaran WHERE cart_id = :cartOwnerId")
    fun updatePembayaran(cartOwnerId: Int, pembayaran: Pembayaran)

    @Query("UPDATE cart_table SET metode_pengiriman = :pengiriman WHERE cart_id = :cartOwnerId")
    fun updatePengiriman(cartOwnerId: Int, pengiriman: Pengiriman)

    @Query("UPDATE cart_table SET total_price = :totalPrice WHERE cart_id = :cartOwnerId")
    fun setCartTotalPrice(cartOwnerId: Int, totalPrice: Int)

    @Query("SELECT * FROM cart_table")
    fun getCart(): LiveData<List<CartEntity>>

    @Query("SELECT cart_id FROM cart_table ORDER BY cart_id DESC LIMIT 1")
    fun getLastCart(): Int

    @Query("SELECT cart_id FROM cart_table ORDER BY cart_id DESC LIMIT 1")
    fun getLastCartLive(): LiveData<Int>
}