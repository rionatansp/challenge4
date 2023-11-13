package binar.academy.challengebinar.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.CartDetailEntity
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.data.RestoranInfo
import binar.academy.challengebinar.databinding.FragmentDetailBinding
import binar.academy.challengebinar.utils.formatSeparator
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import binar.academy.challengebinar.viewmodels.MenuViewModel
import binar.academy.challengebinar.viewmodels.MenuViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var menu_id = 0
    private var qty = 0
    private var totalHarga = 0
    private var lastCartId = 0
    private var menuPrice = 0


    private val menuViewModel: MenuViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val dataSource = RestaurantDatabase.getInstance(application).menuDatabaseDao
        MenuViewModelFactory(dataSource, application)
    }

    private val cartViewModel: CartViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val cartDao = RestaurantDatabase.getInstance(application).cartDatabaseDao
        val cartDetailDao = RestaurantDatabase.getInstance(application).cartDetailDatabaseDao
        CartViewModelFactory(cartDao, cartDetailDao, application)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lastCartId = cartViewModel.getLastCartId()

        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel.getDataMenuDetail().observe(requireActivity()){result ->

            binding.namaMenu.text = result.menu_name
            binding.hargaMenu.text = "Rp. ${result.menu_price?.formatSeparator()}"
            binding.deskripsiMenu.text = result.menu_description
            binding.imgPreview.setImageResource(result.menu_preview)
            binding.deskripsiLokasi.text = RestoranInfo().alamat
            menuPrice = result.menu_price
            menu_id = result.menu_id
        }

        // Mengobserve nilai quantity ke TextView
        cartViewModel.quantity.observe(requireActivity()) { result ->
            qty = result
            cartViewModel.setTotalHarga(menuPrice)
            binding.tvItemCount.text = result.toString()
            totalHarga = cartViewModel.getTotalHarga()
            binding.btnTambahKeranjang.text = "Tambah Ke Keranjang - Rp ${totalHarga.formatSeparator()}"

        }


        // Menghitung jumlah harga item di keranjang

        binding.btnPlus.setOnClickListener {
            cartViewModel.incrementQuantity()
            cartViewModel.setTotalHarga(menuPrice)
        }

        binding.btnMin.setOnClickListener {
            cartViewModel.decrementQuantity()
            cartViewModel.setTotalHarga(menuPrice)
        }



            val oldCart = cartViewModel.getOldCart()
            if (oldCart != null) {
                Log.e("Tracker Dulu -", "oldCart: ${oldCart}")
                Log.e("Tracker Dulu -", "menuId: ${menu_id}")
                Log.e("Tracker Dulu -", "lastCartId: ${lastCartId}")
                cartViewModel.setOldQuantity(oldCart.quantity)
                cartViewModel.setOldTotalHarga(oldCart.subTotal)
            } else {
                cartViewModel.resetQuantity()
            }






        binding.viewMaps.setOnClickListener {
            goToMaps(RestoranInfo().urlAlamat)
        }

        binding.btnArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    override fun onStart() {
        super.onStart()
        // Menuju ke Maintenance Fragment

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        binding.btnTambahKeranjang.setOnClickListener {

            addToCart()
            findNavController().popBackStack()
            bottomNavigationView.selectedItemId = R.id.cartFragment
        }
    }

    fun addToCart() {
        val oldCart = cartViewModel.getOldCart()
            if (oldCart != null) {
                Log.d("Tracker Dulu -", "Masuk ke If")
                cartViewModel.updateCartDetail(cartDetailId = oldCart.cart_detail_id, quantity = qty, subTotal = totalHarga)
            } else {
                Log.d("Tracker Dulu -", "Masuk ke Else")

                cartViewModel.insertCartDetail(
                    CartDetailEntity(
                        quantity = qty,
                        subTotal = totalHarga,
                        cart_owener_id = lastCartId,
                        menu_owner_id = menu_id
                    )
                )
            }

    }


    fun goToMaps(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Google Maps tidak terinstal", Toast.LENGTH_SHORT).show()
        }
    }



//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }
}
