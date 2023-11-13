package binar.academy.challengebinar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_table")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true)
    var menu_id: Int = 0,

    @ColumnInfo(name = "menu_name")
    var menu_name: String = "",

    @ColumnInfo(name = "menu_category")
    var menu_category: String = "",

    @ColumnInfo(name = "menu_price")
    var menu_price: Int = 0,

    @ColumnInfo(name = "menu_quantity")
    var menu_quantitiy: Int = 0,

    @ColumnInfo(name = "menu_preview")
    var menu_preview: Int = 0,

    @ColumnInfo(name = "menu_description")
    var menu_description: String = "",

    @ColumnInfo(name = "menu_isrecommendation")
    var menu_isrecommendation: Boolean = false
)

