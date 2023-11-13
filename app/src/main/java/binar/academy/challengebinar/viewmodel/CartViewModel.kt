package binar.academy.challengebinar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import binar.academy.challengebinar.data.CartDetailEntity
import binar.academy.challengebinar.data.CartDetailWithCartAndMenu
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.dao.CartDatabaseDao
import binar.academy.challengebinar.dao.CartDetailDatabaseDao
import binar.academy.challengebinar.utils.Pembayaran
import binar.academy.challengebinar.utils.Pengiriman
import binar.academy.challengebinar.utils.SharedPreferencesManager
import binar.academy.challengebinar.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CartViewModel(val cartDao: CartDatabaseDao, val cartDetailDao: CartDetailDatabaseDao, application: Application)
    : AndroidViewModel(application){

    /*********************** Punya Cart List *********************/
    val cartList: LiveData<List<CartEntity>> = liveData(viewModelScope.coroutineContext) {
        emitSource(cartDao.getCart())
    }

    fun insertCart(cart: CartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCartDatabase(cart)
        }
    }

    fun updatePembayaran(cartOwnerId: Int, pembayaran: Pembayaran) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updatePembayaranDatabase(cartOwnerId, pembayaran)
            }
        }
    }

    fun updatePengiriman(cartOwnerId: Int, pengiriman: Pengiriman) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updatePengirimanDatabase(cartOwnerId, pengiriman)
            }
        }
    }

    fun updateStatus(cartOwnerId: Int, status: Status) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateStatusDatabase(cartOwnerId, status)
            }
        }
    }

//    fun updateCartPembayaran(cartId: Int) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                updateCartPembayaranDatabase(cartId)
//            }
//        }
//    }

    private fun updateStatusDatabase(cartOwnerId: Int, status: Status) {
        cartDao.updateStatus(cartOwnerId, status)
    }

    private fun updatePembayaranDatabase(cartOwnerId: Int, pembayaran: Pembayaran) {
        cartDao.updatePembayaran(cartOwnerId, pembayaran)
    }

    private fun updatePengirimanDatabase(cartOwnerId: Int, pengiriman: Pengiriman) {
        cartDao.updatePengiriman(cartOwnerId, pengiriman)
    }

    private suspend fun insertCartDatabase(cart: CartEntity) {
        cartDao.insert(cart)
    }

//    private suspend fun updateCartPembayaranDatabase(cartId: Int) {
//        cartDao.updateCartPembayaran()
//    }

    /*********************** Punya Last Cart ID *********************/

//    var lastCart: LiveData<Int> = cartDao.getLastCartLive()
//    fun getLastCart(): Int {
//        return lastCart.value
//    }

    private var _lastCartId = MutableLiveData<Int>()
    val lastCartId: LiveData<Int> get() = _lastCartId

    fun initializeCart() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!SharedPreferencesManager.isCartInitialized) {
                SharedPreferencesManager.isCartInitialized = true
                initializeCartDatabase()
            }
        }
    }

    private fun initializeCartDatabase() {
        cartDao.insert(CartEntity())
    }

    fun setLastCartId() {
        viewModelScope.launch() {
            val lastCart = withContext(Dispatchers.IO) {
                getLastCartIdFromDatabase()
            }
            _lastCartId.value = lastCart
        }
    }

    fun getLastCartId(): Int {
        return lastCartId.value ?: 0
    }



    private suspend fun getLastCartIdFromDatabase(): Int {
        return cartDao.getLastCart()
    }

    /*********************** Punya Cart Detail List *********************/

    var allCartDetailList: LiveData<List<CartDetailEntity>> = cartDetailDao.getAllCartDetail()

    val cartDetailList: LiveData<List<CartDetailEntity>> = _lastCartId.switchMap { lastCartId ->
        liveData(viewModelScope.coroutineContext) {
            emitSource(cartDetailDao.getCartDetail(lastCartId))
        }
    }
    fun insertCartDetail(cartDetail: CartDetailEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Operasi database dijalankan di latar belakang
                insertCartDetailDatabase(cartDetail)
            }
        }
    }

    fun updateCartDetail(cartDetailId: Int, quantity: Int, subTotal: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Operasi database dijalankan di latar belakang
                updateCartDetailDatabase(cartDetailId, quantity, subTotal)
            }
        }
    }

    fun updateCatatanCartDetail(cartDetailId: Int, catatan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCatatanCartDetailDatabase(cartDetailId, catatan)
        }
    }

    fun deleteCartDetail(cartDetailId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteCartDetailDatabase(cartDetailId)
            }
        }
    }

    fun clearCartDetail() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                clearCartDetailDatabase()
            }
        }
    }

    private suspend fun clearCartDetailDatabase() {
        cartDetailDao.clear()
    }

    private suspend fun getCartDetailFromDatabase(cartOwnerId: Int) : LiveData<List<CartDetailEntity>> {
        return cartDetailDao.getCartDetail(cartOwnerId)
    }

    private suspend fun insertCartDetailDatabase(cartDetail: CartDetailEntity) {
        cartDetailDao.insert(cartDetail)
    }

    private suspend fun updateCatatanCartDetailDatabase(cartDetailId: Int, catatan: String) {
        cartDetailDao.updateCatatan(cartDetailId, catatan)
    }

    private suspend fun updateCartDetailDatabase(cartDetailId: Int, quantity: Int, subTotal: Int) {
        cartDetailDao.updateCartDetail(cartDetailId, quantity, subTotal)
    }

    private suspend fun deleteCartDetailDatabase(cartDetailId: Int) {
        cartDetailDao.deleteCartDetail(cartDetailId)
    }


    /*********************** Punya Old Cart Menu *********************/

    private var _oldCartMenu: MutableLiveData<CartDetailEntity> = MutableLiveData()
    val oldCartMenu: LiveData<CartDetailEntity> get() = _oldCartMenu

    fun setOldCart(menuOwnerId: Int, cartOwnerId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Operasi database dijalankan di latar belakang
                _oldCartMenu.postValue(getCartMenuInfo(menuOwnerId, cartOwnerId))
            }
        }
    }

    fun getOldCart(): CartDetailEntity? {
        return oldCartMenu.value
    }

    fun setOldQuantity(oldQuantity: Int) {
        _quantity.value = oldQuantity
    }

    private suspend fun getCartMenuInfo(menuOwnerId: Int, cartOwnerId: Int): CartDetailEntity {
        return cartDetailDao.getCartMenuInfo(menuOwnerId, cartOwnerId)
    }

    /*********************** Punya Total Harga *********************/

    private var _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
    val totalHarga: LiveData<Int> get() = _totalHarga

    fun setOldTotalHarga(oldSubTotal: Int) {
        _totalHarga.value = oldSubTotal
    }

    fun setTotalHarga(menuPrice: Int) {
        _totalHarga.value = menuPrice * _quantity.value!!
    }

    fun resetTotalHarga() {
        _totalHarga.value = 0
    }

    fun getTotalHarga(): Int {
        return totalHarga.value ?: 0
    }


    /*********************** Punya Total Price *********************/

    private var _totalPrice: MutableLiveData<Int> = MutableLiveData(0)
    val totalPrice: LiveData<Int> get() = _totalPrice

//    val cartTotalPrice: LiveData<Int> = liveData(viewModelScope.coroutineContext) {
//        emitSource(cartDetailDao.getCartDetailTotalPrice(1))
//    }

    fun setTotalPrice(cartOwnerId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val totalPrice = getTotalPriceFromDatabase(cartOwnerId) ?: 99
                setCartTotalPriceDatabase(cartOwnerId, totalPrice)
                _totalPrice.postValue(totalPrice)
                Log.d("Track Dulu -", "liveData totalPrice: ${totalPrice}")
            }
        }
    }
//    fun setTotalPricee() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                var lastCartIdd = 0
//                lastCart.switchMap { id ->
//                    Log.d("Track Dulu -", "liveData lastCart Price: ${id}")
//                }
//                Log.d("Track Dulu -", "liveData lastCart Price: ${lastCartIdd}")
////                setCartTotalPriceDatabase(cartOwnerId, totalPrice)
////                Log.d("Track Dulu -", "liveData totalPrice: ${totalPrice}")
//            }
//        }
//    }


    fun getTotalPrice(): Int {
        return totalPrice.value ?: 0
    }


    private suspend fun getTotalPriceFromDatabase(cartOwnerId: Int): Int? {
        return cartDetailDao.getCartDetailTotalPrice(cartOwnerId)
    }

    private suspend fun setCartTotalPriceDatabase(cartOwnerId: Int, totalPrice: Int) {
        cartDao.setCartTotalPrice(cartOwnerId, totalPrice)
    }


    /*********************** Punya Quantity *********************/

    private var _quantity: MutableLiveData<Int> = MutableLiveData(1)
    val quantity: LiveData<Int> get() = _quantity

    fun resetQuantity() {
        _quantity.value = 1
    }

    fun incrementQuantity() {
        _quantity.value = _quantity.value?.plus(1)
    }

    fun decrementQuantity() {
        _quantity.value?.let {
            if (it > 1) {
                _quantity.value = _quantity.value?.minus(1)
            }
        }
    }

    /*********************** cartDetailWithCartAndMenuList *********************/

//    val cartDetailWithCartAndMenuList: LiveData<List<CartDetailWithCartAndMenu>> =
//        lastCart.switchMap { cartId ->
//            cartDetailDao.getCartDetailsWithCartAndMenu(cartId)
//        }

    fun getCartDetailWithCartAndMenuList(cartId: Int): LiveData<List<CartDetailWithCartAndMenu>> {
        return cartDetailDao.getCartDetailsWithCartAndMenu(cartId)
    }

    fun getAllCartDetailWithCartAndMenuList(status: Status): LiveData<List<CartDetailWithCartAndMenu>> {
        return cartDetailDao.getAllCartDetailsWithCartAndMenu(status)
    }

    init {
        initializeCart()
        setLastCartId()
    }

}

// Fungsi untuk Detail Counter Quantity Detail Fragment



//    fun getDatabaseCartDetailList() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                // Operasi database dijalankan di latar belakang
//                _cartDetailList.postValue(getCartDetailListDatabase())
//            }
//        }
//    }







// Fungsi di Fragment Cart untuk mengubah langsung ke database



//    fun getDatabaseLastCartId() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _lastCartId.postValue(getLastCartIdFromDatanase())
//        }
//    }

//    private suspend fun getCartDetailListDatabase(): List<CartDetailEntity> {
//        return cartDetailDao.getCartDetail()
//    }

/*

val cartList: LiveData<List<CartEntity>> = liveData(viewModelScope.coroutineContext) {
    emitSource(cartDao.getCart())
}
//    val cartDetailList: LiveData<List<CartDetailEntity>> = liveData(viewModelScope.coroutineContext) {
//        emitSource(cartDetailDao.getCart())
//    }

val lastCartId: LiveData<Int> = liveData(viewModelScope.coroutineContext) {
    emitSource(cartDao.getLastCart())
}

//    private val _lastCartId = MutableLiveData<Int>()
//    val lastCartId: LiveData<Int> get() = _lastCartId


val cartTotalPrice: LiveData<Int> = liveData(viewModelScope.coroutineContext) {
    emitSource(cartDetailDao.getCartDetailTotalPrice(1))
}

//    val cartDetailWithCartAndMenuList: LiveData<List<CartDetailWithCartAndMenu>> = liveData(viewModelScope.coroutineContext) {
//        emitSource(cartDetailDao.getCartDetailsWithCartAndMenu(lastCartId))
//    }

//    val cartDetailWithCartAndMenuList: LiveData<List<CartDetailWithCartAndMenu>> = Transformations.switchMap(lastCartId) { cartId ->
//        repository.getCartDetailWithCartAndMenuListLiveData(cartId)
//    }

// Live Data hanya untuk mempertahankan counter di detail Fragment
private var _quantity: MutableLiveData<Int> = MutableLiveData(1)
val quantity: LiveData<Int> get() = _quantity

private var _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
val totalHarga: LiveData<Int> get() = _totalHarga

// Khusus untuk merepopulate jika telah memilih menu
private var _oldCartMenu: MutableLiveData<CartDetailEntity> = MutableLiveData()
val oldCartMenu: LiveData<CartDetailEntity> get() = _oldCartMenu

private var _cartDetailList: MutableLiveData<List<CartDetailEntity>> = MutableLiveData()
val cartDetailList: LiveData<List<CartDetailEntity>> get() = _cartDetailList

// fungsi untuk Cart Fragment
fun setCartTotalPrice(cartOwnerId: Int, totalPrice: Int) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Operasi database dijalankan di latar belakang
            setCartTotalPriceDatabase(cartOwnerId, totalPrice)
        }
    }
}

init {
    getDatabaseCartDetailList()
}


// Fungsi untuk Detail Counter Quantity Detail Fragment

fun getCartDetailList(): List<CartDetailEntity>? {
    return cartDetailList.value
}

fun getDatabaseCartDetailList() {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Operasi database dijalankan di latar belakang
            _cartDetailList.postValue(getCartDetailListDatabase())
        }
    }
}

fun getDatabaseOldCartMenu(menuOwnerId: Int, cartOwnerId: Int) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Operasi database dijalankan di latar belakang
            _oldCartMenu.postValue(getCartMenuInfo(menuOwnerId, cartOwnerId))
        }
    }
}

fun getOldCartMenu(): CartDetailEntity? {
    return oldCartMenu.value
}

fun setOldQuantity(oldQuantity: Int) {
    _quantity.value = oldQuantity
}

fun setOldTotalHarga(oldSubTotal: Int) {
    _totalHarga.value = oldSubTotal
}

fun incrementQuantity() {
    _quantity.value = _quantity.value?.plus(1)
}

fun decrementQuantity() {
    _quantity.value?.let {
        if (it > 1) {
            _quantity.value = _quantity.value?.minus(1)
        }
    }
}

fun setTotalHarga(menuPrice: Int) {
    _totalHarga.value = menuPrice * _quantity.value!!
}

// Fungsi di Fragment Cart untuk mengubah langsung ke database

fun getCartDetailWithCartAndMenuList(cartId: Int): LiveData<List<CartDetailWithCartAndMenu>> {
    return cartDetailDao.getCartDetailsWithCartAndMenu(cartId)
}

//    fun getDatabaseLastCartId() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _lastCartId.postValue(getLastCartIdFromDatanase())
//        }
//    }

fun updateCatatanCartDetail(cartDetailId: Int, catatan: String) {
    viewModelScope.launch {
        updateCatatanCartDetailDatabase(cartDetailId, catatan)
    }
}

fun insertCart(cart: CartEntity) {
    viewModelScope.launch {
        insertCartDatabase(cart)
    }
}

fun insertCartDetail(cartDetail: CartDetailEntity) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Operasi database dijalankan di latar belakang
            insertCartDetailDatabase(cartDetail)
        }
    }
}

fun updateCartDetail(cartDetailId: Int, quantity: Int, subTotal: Int) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Operasi database dijalankan di latar belakang
            updateCartDetailDatabase(cartDetailId, quantity, subTotal)
        }
    }
}

fun deleteCartDetail(cartDetailId: Int) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            deleteCartDetailDatabase(cartDetailId)
        }
    }
}


private suspend fun setCartTotalPriceDatabase(cartOwnerId: Int, totalPrice: Int) {
    cartDao.setCartTotalPrice(cartOwnerId, totalPrice)
}

private suspend fun deleteCartDetailDatabase(cartDetailId: Int) {
    cartDetailDao.deleteCartDetail(cartDetailId)
}

private suspend fun updateCatatanCartDetailDatabase(cartDetailId: Int, catatan: String) {
    cartDetailDao.updateCatatan(cartDetailId, catatan)
}

private suspend fun updateCartDetailDatabase(cartDetailId: Int, quantity: Int, subTotal: Int) {
    cartDetailDao.updateCartDetail(cartDetailId, quantity, subTotal)
}

private suspend fun getCartMenuInfo(menuOwnerId: Int, cartOwnerId: Int): CartDetailEntity {
    return cartDetailDao.getCartMenuInfo(menuOwnerId, cartOwnerId)
}

private suspend fun insertCartDatabase(cart: CartEntity) {
    cartDao.insert(cart)
}

private suspend fun insertCartDetailDatabase(cartDetail: CartDetailEntity) {
    cartDetailDao.insert(cartDetail)
}


private suspend fun getCartDetailListDatabase(): List<CartDetailEntity> {
    return cartDetailDao.getCartDetail()
}


 */