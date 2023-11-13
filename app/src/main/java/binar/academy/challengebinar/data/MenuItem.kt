package binar.academy.challengebinar.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem (
    val name: String,
    val price: Int,
    var count: Int,
    val itemPreview: Int,
    val deskripsi: String,
    var bestSeller: Int = 0
) : Parcelable

