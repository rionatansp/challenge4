package binar.academy.challengebinar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.data.Menu
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.databinding.ActivityMainBinding
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import binar.academy.challengebinar.viewmodels.MenuViewModel
import binar.academy.challengebinar.viewmodels.MenuViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.navContainer)

        // Menemukan referensi ke bottom navigation view
        binding.bottomNavView.setupWithNavController(navController)

        val menuDao = RestaurantDatabase.getInstance(this.application).menuDatabaseDao

        binding.btnTrigger.visibility = View.GONE


        // Menghide bottom navigation view pada detail fragment
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            // Cek apakah fragment saat ini adalah fragment yang ingin Anda sembunyikan bottom navigation view-nya
            if (destination.id == R.id.detailFragment || destination.id == R.id.maintenanceFragment ||
                destination.id == R.id.konfirmasiPesananFragment || destination.id == R.id.riwayatBelanjaFragment) {
                // Sembunyikan bottom navigation view
                binding.bottomNavView.visibility = View.GONE
            } else {
                // Tampilkan kembali bottom navigation view jika fragment lain aktif
                binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }
}

//        val dataSource = RestaurantDatabase.getInstance(this.application).menuDatabaseDao
//        viewModel = ViewModelProvider(this, MenuViewModelFactory(dataSource, this.application)).get(MenuViewModel::class.java)
//
//        val cartDao = RestaurantDatabase.getInstance(this.application).cartDatabaseDao
//        val cartDetailDao = RestaurantDatabase.getInstance(this.application).cartDetailDatabaseDao
//        viewModelCart = ViewModelProvider(this, CartViewModelFactory(cartDao, cartDetailDao, this.application)).get(CartViewModel::class.java)
//
//
//        // inisiasi awal agar data bisa terfetch ketika activity pertama kali terbuat
////        viewModel.menuList.observe(this) {result ->
////            viewModel.setupMenu()
////            Log.e("SimpleDatabase", Menu.menus.toString())
////        }
//
//
////        binding.btnTrigger.visibility = View.GONE
//        binding.btnTrigger.setOnClickListener {
////            viewModel.clearMenu()
////            viewModel.insertMenu(MenuItemEntity(menu_name = "Rionatan"))
////            viewModel.deleteAllMenu()
////            viewModelCart.cartList.observe(this){result->
////                Log.e("Tracker Dulu", result.toString())
////            }
////            viewModel.deleteMenu(MenuItemEntity(menu_id = 3))
//
////            dataSource.insert(MenuItemEntity())
//            viewModel.menuList.observe(this){result ->
//                Log.e("Tracker Dulu", result.toString())
//            }
//        }