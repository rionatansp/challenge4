package binar.academy.challengebinar.data

import androidx.room.Embedded
import androidx.room.Relation

class CartDetailWithCartAndMenu(
    @Embedded val cartDetailEntity: CartDetailEntity,
    @Relation(
        parentColumn = "cart_owner_id",
        entityColumn = "cart_id"
    )
    val cartEntity: CartEntity,
    @Relation(
        parentColumn = "menu_owner_id",
        entityColumn = "menu_id"
    )
    val menuItemEntity: MenuItemEntity
)