package binar.academy.challengebinar.ui

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.data.UserEntitiy
import binar.academy.challengebinar.databinding.FragmentProfileBinding
import binar.academy.challengebinar.viewmodel.UserViewModel
import binar.academy.challengebinar.viewmodel.UserViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isToggleImageON = false

    private val userViewModel: UserViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val userDao = RestaurantDatabase.getInstance(application).userDatabaseDao
        UserViewModelFactory(userDao, application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgProfile.setOnClickListener {
            isToggleImageON = !isToggleImageON
            if (isToggleImageON) {
                binding.imgProfile.setImageResource(R.drawable.sorry)
            } else {
                binding.imgProfile.setImageResource(R.drawable.rio_profile)
            }
        }

        // populate
        userViewModel.users.observe(viewLifecycleOwner){ userData ->
            binding.inputUsername.text = Editable.Factory.getInstance().newEditable(userData[0].username)
            binding.inputEmail.text = Editable.Factory.getInstance().newEditable(userData[0].email)
            binding.inputPassword.text = Editable.Factory.getInstance().newEditable(userData[0].password)
            binding.inputTelepon.text = Editable.Factory.getInstance().newEditable(userData[0].telephone)
        }

        binding.btnEditProfile.setOnClickListener {
            binding.btnSimpanPerubahan.visibility = View.VISIBLE
            binding.btnRiwayatPesanan.visibility = View.GONE
            binding.inputUsername.isEnabled = true
            binding.inputPassword.isEnabled = true
            binding.inputEmail.isEnabled = true
            binding.inputTelepon.isEnabled = true
        }

        binding.btnSimpanPerubahan.setOnClickListener {
            binding.btnSimpanPerubahan.visibility = View.GONE
            binding.btnRiwayatPesanan.visibility = View.VISIBLE
            binding.inputUsername.isEnabled = false
            binding.inputPassword.isEnabled = false
            binding.inputEmail.isEnabled = false
            binding.inputTelepon.isEnabled = false

            val userUpdate = userViewModel.getUser() ?: UserEntitiy()
            userUpdate.username = binding.inputUsername.text.toString()
            userUpdate.password = binding.inputPassword.text.toString()
            userUpdate.email = binding.inputEmail.text.toString()
            userUpdate.telephone = binding.inputTelepon.text.toString()

            userViewModel.updateUser(userUpdate)
        }

        binding.btnRiwayatPesanan.setOnClickListener {
            val actionToRiwayatBelanjaFragment = ProfileFragmentDirections.actionProfileFragmentToRiwayatBelanjaFragment()
            findNavController().navigate(actionToRiwayatBelanjaFragment)
        }
    }

}