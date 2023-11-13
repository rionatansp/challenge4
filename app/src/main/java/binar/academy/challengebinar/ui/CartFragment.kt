package binar.academy.challengebinar.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.databinding.FragmentCartBinding
import binar.academy.challengebinar.adapter.CartAdapter
import binar.academy.challengebinar.utils.formatSeparator
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private var lastCartID = 0

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

        // Inisialisasi last cart
        lastCartID = cartViewModel.getLastCartId()

        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.cartDetailList.observe(viewLifecycleOwner){result ->
            cartViewModel.setTotalPrice(lastCartID)
            Log.d("Tracker Dulu", "cardList Cart Fragment: ${result}")
        }

        setupCartRecyclerView()


        binding.btnPesan.setOnClickListener {
            val actionToKonfirmasiPesananFragment = CartFragmentDirections.actionCartFragmentToKonfirmasiPesananFragment()
            findNavController().navigate(actionToKonfirmasiPesananFragment)

        }
    }


    fun setupCartRecyclerView() {
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = CartAdapter(false)
        binding.rvCart.adapter = adapter
        binding.rvCart.itemAnimator = null

            cartViewModel.getCartDetailWithCartAndMenuList(lastCartID).observe(viewLifecycleOwner){result ->
                if (result.isNullOrEmpty()) {
                    binding.btnPesan.visibility = View.GONE
                    binding.cartKosong.visibility = View.VISIBLE
                } else {
                    binding.btnPesan.visibility = View.VISIBLE
                    binding.cartKosong.visibility = View.GONE
                }

                binding.tvTotalHarga.text = "Rp. ${cartViewModel.getTotalPrice().formatSeparator()}"

                Log.e("Tracker Dulu", "innerJoin: ${result}")
                adapter.submitData(result)
            }


        adapter.setOnBtnCatatanClickListener { cartDetailId, catatan ->
            Log.e("Tracker Dulu - ", catatan)
            cartViewModel.updateCatatanCartDetail(cartDetailId, catatan)
        }

        adapter.setOnBtnPlusClickListener { carDetailId, quantity, menu_price  ->
            var qty = quantity + 1
            var totalPrice = qty * menu_price
            cartViewModel.updateCartDetail(carDetailId, qty, totalPrice)
        }

        adapter.setOnBtnDeleteClickListener { carDetailId  ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Apakah Anda yakin akan menghapus item ini?")
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Yes") { dialog, which ->
                    // Respond to positive button press
                    cartViewModel.deleteCartDetail(carDetailId)
                }
                .show()
        }

        adapter.setOnBtnMinClickListener { carDetailId, quantity, menu_price  ->
            var qty = quantity
            if (qty > 1) {
                qty--
                var totalPrice = qty * menu_price
                cartViewModel.updateCartDetail(carDetailId, qty, totalPrice)
            }

        }
    }

}