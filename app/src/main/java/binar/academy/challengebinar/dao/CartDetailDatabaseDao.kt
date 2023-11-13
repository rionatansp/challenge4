package binar.academy.challengebinar.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import binar.academy.challengebinar.data.CartDetailEntity
import binar.academy.challengebinar.data.CartDetailWithCartAndMenu
import binar.academy.challengebinar.utils.Status

@Dao
interface CartDetailDatabaseDao {
    @Insert
    fun insert(cartDetail: CartDetailEntity)

    @Update
    fun update(cartDetail: CartDetailEntity)

    @Delete
    fun delete(cartDetail: CartDetailEntity)

    @Query("DELETE FROM cart_detail_table")
    fun clear()

    @Query("SELECT * FROM cart_detail_table")
    fun getAllCartDetail(): LiveData<List<CartDetailEntity>>

    @Query("DELETE FROM cart_detail_table WHERE cart_detail_id = :cartDetailId")
    fun deleteCartDetail(cartDetailId: Int)

    @Query("SELECT * FROM cart_detail_table WHERE cart_owner_id = :cartOwnerId")
    fun getCartDetail(cartOwnerId: Int): LiveData<List<CartDetailEntity>>

    @Query("SELECT * FROM cart_detail_table WHERE menu_owner_id = :menuOwnerId AND cart_owner_id = :cartOwnerId")
    fun getCartMenuInfo(menuOwnerId: Int, cartOwnerId: Int): CartDetailEntity

    @Query("UPDATE cart_detail_table SET quantity = :quantity, sub_total = :subTotal WHERE cart_detail_id = :cartDetailId")
    fun updateCartDetail(cartDetailId: Int, quantity: Int, subTotal: Int)

    @Query("UPDATE cart_detail_table SET catatan = :catatan WHERE cart_detail_id = :cartDetailId")
    fun updateCatatan(cartDetailId: Int, catatan: String)

    @Transaction
    @Query("SELECT * FROM cart_detail_table INNER JOIN cart_table ON cart_owner_id = cart_id INNER JOIN menu_table ON menu_owner_id = menu_id WHERE cart_owner_id = :cartId")
    fun getCartDetailsWithCartAndMenu(cartId: Int?): LiveData<List<CartDetailWithCartAndMenu>>

    @Transaction
    @Query("SELECT * FROM cart_detail_table INNER JOIN cart_table ON cart_owner_id = cart_id INNER JOIN menu_table ON menu_owner_id = menu_id WHERE status = :status")
    fun getAllCartDetailsWithCartAndMenu(status: Status): LiveData<List<CartDetailWithCartAndMenu>>

    // Melakukan perhitungan total price untuk di tabel cart

    @Query("SELECT sum(sub_total) FROM cart_detail_table WHERE cart_owner_id = :cartOwnerId")
    fun getCartDetailTotalPrice(cartOwnerId: Int?): Int
}