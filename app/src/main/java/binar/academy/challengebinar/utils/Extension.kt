package binar.academy.challengebinar.utils

import java.util.*

fun Int.formatSeparator(): String {
    val formatter = java.text.NumberFormat.getInstance(Locale.getDefault())
    return formatter.format(this)
}

fun Long.formatSeparator(): String {
    val formatter = java.text.NumberFormat.getInstance(Locale.getDefault())
    return formatter.format(this)
}

