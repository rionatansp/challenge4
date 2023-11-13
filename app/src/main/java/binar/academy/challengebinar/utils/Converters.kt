package binar.academy.challengebinar.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromPengiriman(value: Pengiriman): String {
        return value.name
    }

    @TypeConverter
    fun toPengiriman(value: String): Pengiriman {
        return enumValueOf(value)
    }

    @TypeConverter
    fun fromPembayaran(value: Pembayaran): String {
        return value.name
    }

    @TypeConverter
    fun toPembayaran(value: String): Pembayaran {
        return enumValueOf(value)
    }

    @TypeConverter
    fun fromStatus(value: Status): String {
        return value.name
    }

    @TypeConverter
    fun toStatus(value: String): Status {
        return enumValueOf(value)
    }
}