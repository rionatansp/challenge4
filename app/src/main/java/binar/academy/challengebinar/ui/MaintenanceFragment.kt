package binar.academy.challengebinar.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import binar.academy.challengebinar.databinding.FragmentHomeBinding
import binar.academy.challengebinar.databinding.FragmentMaintenanceBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MaintenanceFragment : Fragment() {
    private var _binding: FragmentMaintenanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackHome.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}