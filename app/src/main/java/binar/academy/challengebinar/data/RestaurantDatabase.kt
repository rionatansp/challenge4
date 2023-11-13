package binar.academy.challengebinar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import binar.academy.challengebinar.dao.CartDatabaseDao
import binar.academy.challengebinar.dao.CartDetailDatabaseDao
import binar.academy.challengebinar.dao.MenuDatabaseDao
import binar.academy.challengebinar.dao.UserDatabaseDao
import binar.academy.challengebinar.utils.Converters
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlin.concurrent.Volatile

@Database(entities = [MenuItemEntity::class, CartEntity::class, CartDetailEntity::class, UserEntitiy::class], version = 79, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract val menuDatabaseDao: MenuDatabaseDao
    abstract val cartDatabaseDao: CartDatabaseDao
    abstract val cartDetailDatabaseDao: CartDetailDatabaseDao
    abstract val userDatabaseDao: UserDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): RestaurantDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RestaurantDatabase::class.java,
                        "simple_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

//@Database(entities = [MenuItemEntity::class, CartEntity::class, CartDetailEntity::class], version = 37, exportSchema = false)
//abstract class RestaurantDatabase : RoomDatabase() {
//
//    abstract val menuDatabaseDao: MenuDatabaseDao
//    abstract val cartDatabaseDao: CartDatabaseDao
//    abstract val cartDetailDatabaseDao: CartDetailDatabaseDao
//
//    companion object {
//
//        @kotlin.jvm.Volatile
//        private var INSTANCE: RestaurantDatabase? = null
//
//        fun getInstance(context: Context): RestaurantDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        RestaurantDatabase::class.java,
//                        "simple_database"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .allowMainThreadQueries()
//                        .addCallback(roomCallback)
//                        .build()
//                    INSTANCE = instance
//                }
//                return  instance
//            }
//        }
//
//         //Fungsi inisialisasi pertama kali database dibuat
//        private val roomCallback = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                // Memasukkan data awal
//                CoroutineScope(Dispatchers.IO).launch {
//                    INSTANCE?.menuDatabaseDao?.insertInitial(InitialDataUtil.getInitialDataMenu())
//                    INSTANCE?.cartDatabaseDao?.insert(CartEntity())
//                }
//            }
//        }
//    }
//}