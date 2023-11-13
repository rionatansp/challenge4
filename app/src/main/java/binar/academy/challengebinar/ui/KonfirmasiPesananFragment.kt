package binar.academy.challengebinar.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.databinding.DialogPesananBerhasilBinding
import binar.academy.challengebinar.databinding.FragmentKonfirmasiPesananBinding
import binar.academy.challengebinar.adapter.CartAdapter
import binar.academy.challengebinar.adapter.RiwayatBelanjaAdapter
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.utils.Pembayaran
import binar.academy.challengebinar.utils.Pengiriman
import binar.academy.challengebinar.utils.Status
import binar.academy.challengebinar.utils.formatSeparator
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class KonfirmasiPesananFragment : Fragment() {
    private var _binding: FragmentKonfirmasiPesananBinding? = null
    private val binding get() = _binding!!
    private var lastCartID = 0

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
        //
        lastCartID = cartViewModel.getLastCartId()

        // Inflate the layout for this fragment
        _binding = FragmentKonfirmasiPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRecyclerView()

        var selectedRadioButtonPengiriman: Int = binding.radioGroupMetodePengiriman.checkedRadioButtonId
        var selectedRadioButtonPembayaran: Int = binding.radioGroupMetodePembayaran.checkedRadioButtonId

        binding.radioGroupMetodePengiriman.setOnCheckedChangeListener { _, checkedId ->
            selectedRadioButtonPengiriman = checkedId
            handleMetodePengiriman(selectedRadioButtonPengiriman)
        }

        handleMetodePengiriman(selectedRadioButtonPengiriman)

        binding.radioGroupMetodePembayaran.setOnCheckedChangeListener { _, checkedId ->
            selectedRadioButtonPembayaran = checkedId
            handleMetodePembayaran(selectedRadioButtonPembayaran)
        }

        handleMetodePembayaran(selectedRadioButtonPembayaran)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun handleMetodePengiriman(selectedRadioButton: Int) {

        when(selectedRadioButton) {
            R.id.radioButtonAmbilSendiri -> {
                cartViewModel.updatePengiriman(lastCartID, Pengiriman.AMBIL_SENDIRI)
            }
            R.id.radioButtonDikirim -> {
                cartViewModel.updatePengiriman(lastCartID, Pengiriman.DIKIRIM)
            }
        }
    }

    private fun handleMetodePembayaran(selectedRadioButton: Int) {
        when(selectedRadioButton) {
            R.id.radioButtonTunai -> {
                cartViewModel.updatePembayaran(lastCartID, Pembayaran.TUNAI)
            }
            R.id.radioButtonDompetDigital -> {
                cartViewModel.updatePembayaran(lastCartID, Pembayaran.DIGITAL)
            }
        }
    }

    private fun setupCartRecyclerView() {
        binding.rvKonfirmasiCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        val adapter = CartAdapter(true)
//        binding.rvKonfirmasiCart.adapter = adapter
        binding.rvKonfirmasiCart.itemAnimator = null

        lateinit var adapter: RiwayatBelanjaAdapter

        cartViewModel.getCartDetailWithCartAndMenuList(lastCartID).observe(viewLifecycleOwner){result ->
            Log.e("Tracker Aja", "innerJoin: ${result}")
//            adapter.submitData(result)
            adapter = RiwayatBelanjaAdapter(result)
            binding.rvKonfirmasiCart.adapter = adapter

            var totalPrice = result[0].cartEntity.totalPrice
            binding.tvTotalBayar.text = "Rp. ${totalPrice.formatSeparator()}"

            if (result[0].cartEntity.metodePengiriman == Pengiriman.AMBIL_SENDIRI) {
                binding.radioGroupMetodePengiriman.check(R.id.radioButtonAmbilSendiri)
            } else if (result[0].cartEntity.metodePengiriman == Pengiriman.DIKIRIM) {
                binding.radioGroupMetodePengiriman.check(R.id.radioButtonDikirim)
            }

            if (result[0].cartEntity.metodePembayaran == Pembayaran.TUNAI) {
                binding.radioGroupMetodePembayaran.check(R.id.radioButtonTunai)
            } else if (result[0].cartEntity.metodePembayaran == Pembayaran.DIGITAL) {
                binding.radioGroupMetodePembayaran.check(R.id.radioButtonDompetDigital)
            }
        }

        binding.btnPesan.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogBinding = DialogPesananBerhasilBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        dialogBinding.btnKembali.setOnClickListener {
            dialog.dismiss()
            backToHome()
        }

        dialog.show()
    }

    private fun backToHome() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        cartViewModel.updateStatus(lastCartID, Status.CHECKOUT)
        cartViewModel.insertCart(CartEntity())
        cartViewModel.resetTotalHarga()
        cartViewModel.cartList.observe(viewLifecycleOwner) {result ->
            Log.e("Tracker Dulu", "cartList Konfirmasi Pesanan: ${result}")
        }
        findNavController().popBackStack()
        bottomNavigationView.selectedItemId = R.id.homeFragment

    }

    companion object {

    }
}