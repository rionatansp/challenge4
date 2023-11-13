package binar.academy.challengebinar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_detail_table",
    foreignKeys = [
        ForeignKey(entity = CartEntity::class, parentColumns = ["cart_id"], childColumns = ["cart_owner_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = MenuItemEntity::class, parentColumns = ["menu_id"], childColumns = ["menu_owner_id"], onDelete = ForeignKey.CASCADE)
    ]

)
data class CartDetailEntity(
    @PrimaryKey(autoGenerate = true)
    var cart_detail_id: Int = 0,

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,

    @ColumnInfo(name = "sub_total")
    var subTotal: Int = 0,

    @ColumnInfo(name = "catatan")
    var catatan: String = "",

    @ColumnInfo(name = "cart_owner_id")
    var cart_owener_id: Int = 0,

    @ColumnInfo(name = "menu_owner_id")
    var menu_owner_id: Int = 0
)