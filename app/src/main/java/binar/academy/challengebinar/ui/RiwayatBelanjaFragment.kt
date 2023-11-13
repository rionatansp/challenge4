package binar.academy.challengebinar.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.challengebinar.adapter.CartAdapter
import binar.academy.challengebinar.adapter.RiwayatBelanjaAdapter
import binar.academy.challengebinar.data.CartDetailEntity
import binar.academy.challengebinar.data.CartDetailWithCartAndMenu
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.databinding.FragmentRiwayatBelanjaBinding
import binar.academy.challengebinar.utils.Status
import binar.academy.challengebinar.utils.formatSeparator
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RiwayatBelanjaFragment : Fragment() {
    private var _binding: FragmentRiwayatBelanjaBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val cartDao = RestaurantDatabase.getInstance(application).cartDatabaseDao
        val cartDetailDao = RestaurantDatabase.getInstance(application).cartDetailDatabaseDao
        CartViewModelFactory(cartDao, cartDetailDao, application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentRiwayatBelanjaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDeleteRiwayat.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Apakah kamu yakin mau hapus riwayat belanja?")
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Iya dong, takut dimarah") { dialog, which ->
                    // Respond to positive button press
                    cartViewModel.clearCartDetail()
                }
                .show()

        }
    }

    fun setupRecyclerView() {


//        cartViewModel.cartList.observe(viewLifecycleOwner){result ->
//            Log.e("Tracker Dulu", "cartList Riwayat: ${result}")
//        }
//
//        cartViewModel.allCartDetailList.observe(viewLifecycleOwner){result ->
//            Log.e("Tracker Dulu", "cartDetailList Riwayat: ${result}")
//        }

        cartViewModel.getAllCartDetailWithCartAndMenuList(Status.CHECKOUT).observe(viewLifecycleOwner) { result ->
            if (result.isNullOrEmpty()) {
                Log.e("Tracker Dulu", "result: ${result}")
                binding.btnDeleteRiwayat.visibility = View.GONE
                binding.riwayatKosong.visibility = View.VISIBLE
            } else {
                binding.btnDeleteRiwayat.visibility = View.VISIBLE
                binding.riwayatKosong.visibility = View.GONE
            }

            // Mengelompokkan data berdasarkan cartEntity.cart_id
            val groupedData: Map<Int, List<CartDetailWithCartAndMenu>> = result.groupBy { it.cartEntity.cart_id }

            // Mengonversi Map ke dalam format yang dapat ditampilkan oleh adapter
            val riwayatData: List<Any> = groupedData.flatMap { entry ->
                val cartId = entry.key
                val cartDetailList = entry.value
                val cartEntity = cartDetailList.first().cartEntity
                val groupHeader = "Riwayat Belanja Ke: $cartId"
                listOf(groupHeader) + cartDetailList + cartEntity
            }

            Log.e("Tracker Dulu", "Riwayat Data: ${riwayatData}")


            // Menyiapkan RecyclerView
            binding.rvRiwayatBelanja.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvRiwayatBelanja.itemAnimator = null

            // Mengatur adapter
            val adapter = RiwayatBelanjaAdapter(riwayatData)
            binding.rvRiwayatBelanja.adapter = adapter
        }
    }

    companion object {

    }
}