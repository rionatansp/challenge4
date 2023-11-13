package binar.academy.challengebinar.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences
    private var sharePreferenceName = "SharedPreferenceBinar"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharePreferenceName, Context.MODE_PRIVATE)
    }

    var isGrid: Boolean

        set(value) {
            sharedPreferences.edit {
                putBoolean("IS_GRID", value).apply()
            }
        }

        get() = sharedPreferences.getBoolean("IS_GRID", false)

    var isMenuInitialized: Boolean
        set(value) {
            sharedPreferences.edit {
                putBoolean("ISMENU_INITIALIZED", value).apply()
            }
        }

        get() = sharedPreferences.getBoolean("ISMENU_INITIALIZED", false)

    var isCartInitialized: Boolean
        set(value) {
            sharedPreferences.edit {
                putBoolean("ISCART_INITIALIZED", value).apply()
            }
        }

        get() = sharedPreferences.getBoolean("ISCART_INITIALIZED", false)

    fun writeData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun readData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}