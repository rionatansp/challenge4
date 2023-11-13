package binar.academy.challengebinar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntitiy (
    @PrimaryKey(autoGenerate = true)
    var user_id: Int = 0,

    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "password")
    var password: String = "",

    @ColumnInfo(name = "telephone")
    var telephone: String = ""
)