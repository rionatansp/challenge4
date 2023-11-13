package binar.academy.challengebinar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import binar.academy.challengebinar.utils.Pembayaran
import binar.academy.challengebinar.utils.Pengiriman
import binar.academy.challengebinar.utils.Status

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    var cart_id: Int = 0,

    @ColumnInfo(name = "metode_pembayaran")
    var metodePembayaran: Pembayaran = Pembayaran.TUNAI,

    @ColumnInfo(name = "metode_pengiriman")
    var metodePengiriman: Pengiriman = Pengiriman.AMBIL_SENDIRI,

    @ColumnInfo(name = "total_price")
    var totalPrice: Long = 0L,

    @ColumnInfo(name = "status")
    var status: Status = Status.PENDING
)
