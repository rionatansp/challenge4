package binar.academy.challengebinar.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.data.RestaurantDatabase
import binar.academy.challengebinar.databinding.FragmentHomeBinding
import binar.academy.challengebinar.adapter.MenuAdapter
import binar.academy.challengebinar.adapter.RekomendasiMenuAdapter
import binar.academy.challengebinar.utils.SharedPreferencesManager
import binar.academy.challengebinar.viewmodel.CartViewModel
import binar.academy.challengebinar.viewmodel.CartViewModelFactory
import binar.academy.challengebinar.viewmodel.UserViewModel
import binar.academy.challengebinar.viewmodel.UserViewModelFactory
import binar.academy.challengebinar.viewmodels.MenuViewModel
import binar.academy.challengebinar.viewmodels.MenuViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val menuViewModel: MenuViewModel by activityViewModels {
        val application = requireNotNull(activity).application
        val dataSource = RestaurantDatabase.getInstance(application).menuDatabaseDao
        MenuViewModelFactory(dataSource, application)
    }

    private val cartViewModel: CartViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val cartDao = RestaurantDatabase.getInstance(application).cartDatabaseDao
        val cartDetailDao = RestaurantDatabase.getInstance(application).cartDetailDatabaseDao
        CartViewModelFactory(cartDao, cartDetailDao, application)
    }

    private val userViewModel: UserViewModel by activityViewModels {
        val application = requireNotNull(this.activity).application
        val userDao = RestaurantDatabase.getInstance(application).userDatabaseDao
        UserViewModelFactory(userDao, application)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        SharedPreferencesManager.init(requireContext())

        userViewModel.setUserData()
        cartViewModel.setLastCartId()
        cartViewModel.lastCartId.observe(requireActivity()){result ->
            Log.e("Tracker Dulu -", "lastCartId onCreateview: ${result}")
        }


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("Tracker Dulu", "lastCartId homeFragment: ${cartViewModel.getLastCartId()}")

        menuViewModel.menuRekomendasi.observe(requireActivity()){menuRekomendasi ->
            setupMenuRekomendasi(menuRekomendasi)
        }

        setupMenu()
        setupChangeLayout()
    }

    fun setupChangeLayout() {
        binding.btnChangeLayout.setOnClickListener {
            SharedPreferencesManager.isGrid = !SharedPreferencesManager.isGrid
            setupMenu()
        }
    }

    fun setupMenu() {
        menuViewModel.menuMakanan.observe(requireActivity()){menuMakanan ->
            var isMakanan = true
            setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menuMakanan, isMakanan)
        }

        menuViewModel.menuMinuman.observe(requireActivity()){menuMinuman ->
            var isMakanan = false
            setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menuMinuman, isMakanan)
        }
    }

    // bottomNavigationView jika ditaruh di onViewCreated() saat pertama kali dijalankan
    // akan bernilai null. Jadi kalau diakses selectedItemId nya aplikasi akan forceclose
    override fun onStart() {
        super.onStart()
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        binding.btnChart.setOnClickListener {
            // Set item terpilih di BottomNavigationView
            bottomNavigationView.selectedItemId = R.id.cartFragment
        }

        binding.btnUser.setOnClickListener {
            // Set item terpilih di BottomNavigationView
            bottomNavigationView.selectedItemId = R.id.profileFragment
        }
    }

    fun setupMenuRekomendasi(menu: List<Any>) {
        var menuRekomendasi = menu as ArrayList<MenuItemEntity>

        val adapter = RekomendasiMenuAdapter(menuRekomendasi)
        binding.rvRekomendasiMenu.adapter = adapter
        binding.rvRekomendasiMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        adapter.setOnItemClickListener { menuItem ->
//            Toast.makeText(requireContext(), menuItem.menu_name, Toast.LENGTH_SHORT).show()

            menuViewModel.setMenuDetail(menuItem)
            menuViewModel.getDataMenuDetail().observe(requireActivity()) {result ->
                val lastCartId = cartViewModel.getLastCartId()
                cartViewModel.setOldCart(result.menu_id, lastCartId)
            }
            val actionToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            findNavController().navigate(actionToDetailFragment)
        }

    }


    fun setupRecyclerViewMenu(isGrid: Boolean, menus: List<Any>, isMakanan: Boolean) {
        SharedPreferencesManager.isGrid = isGrid
        val adapter = MenuAdapter(SharedPreferencesManager.isGrid, menus.filterNot { it is String })
        binding.btnChangeLayout.setImageResource(R.drawable.icon_list_layout)

        lateinit var recyclerView: RecyclerView

        if (isMakanan) {
            recyclerView = binding.rvMenuMakanan
            recyclerView.adapter = adapter
        } else {
            recyclerView = binding.rvMenuMinuman
            recyclerView.adapter = adapter
        }

        if (isGrid) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.btnChangeLayout.setImageResource(R.drawable.icon_list_layout)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.btnChangeLayout.setImageResource(R.drawable.icon_grid_layout)
        }

        adapter.setOnItemClickListener { menuItem ->
//            Toast.makeText(requireContext(), menuItem.menu_name, Toast.LENGTH_SHORT).show()

            menuViewModel.setMenuDetail(menuItem)
            menuViewModel.getDataMenuDetail().observe(requireActivity()) {result ->
                val lastCartId = cartViewModel.getLastCartId()
                cartViewModel.setOldCart(result.menu_id, lastCartId)
            }
            val actionToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            findNavController().navigate(actionToDetailFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

/*
       private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   private val menuViewModel: MenuViewModel by activityViewModels {
       val application = requireNotNull(activity).application
       val dataSource = RestaurantDatabase.getInstance(application).menuDatabaseDao
       MenuViewModelFactory(dataSource, application)
   }


   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {

       // Inflate the layout for this fragment
       _binding = FragmentHomeBinding.inflate(inflater, container, false)
       SharedPreferencesManager.init(requireContext())

       // Deklarasi view model dan integrasinya dengan database

//        var menus: List<Any> = mutableListOf()
//        var menuRekomendasi: List<Any> = mutableListOf()
//        menuViewModel.menuList.observe(requireActivity()) {data ->
//            Log.e("Tracker Dulu", data.toString())
//
//            // Logika untuk recycler view menu makanan
//            menus += "Minuman"
//            menus += menuViewModel.getMenuMakanan()
//            menus += "Makanan"
//            menus += menuViewModel.getMenuMinuman()
//
//            menuRekomendasi = menuViewModel.getMenuRekomendasi()
//
//            setupMenuRekomendasi(menuRekomendasi)
//            setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menus)
//
//            // Setup Change Layout Listener
//            binding.btnChangeLayout.setOnClickListener {
//                SharedPreferencesManager.isGrid = !SharedPreferencesManager.isGrid
//                setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menus)
//            }
//        }

//        var menus: List<Any> = mutableListOf()
//        var menuRekomendasi: List<Any> = mutableListOf()
//        menuViewModel.menuList.observe(requireActivity()) {data ->
//            Log.e("Tracker Dulu - ", data.toString())
//
//            // Logika untuk recycler view menu makanan
//            menus += "Minuman"
//            menuViewModel.menuMinuman.observe(requireActivity()){minuman ->
//                menus += minuman
//            }
//            menus += "Makanan"
//            menuViewModel.menuMakanan.observe(requireActivity()){makanan ->
//                menus += makanan
//            }
//
//            menuViewModel.menuRekomendasi.observe(requireActivity()){rekomendasi ->
//                menuRekomendasi = rekomendasi
//                setupMenuRekomendasi(menuRekomendasi)
//            }
//
//            setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menus)
//
//            // Setup Change Layout Listener
//            binding.btnChangeLayout.setOnClickListener {
//                SharedPreferencesManager.isGrid = !SharedPreferencesManager.isGrid
//                setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menus)
//            }
//        }


       menuViewModel.menuRekomendasi.observe(requireActivity()){menuRekomendasi ->
           setupMenuRekomendasi(menuRekomendasi)
       }

       setupMenu()
       setupChangeLayout()

       return binding.root
   }

   fun setupChangeLayout() {
       binding.btnChangeLayout.setOnClickListener {
           SharedPreferencesManager.isGrid = !SharedPreferencesManager.isGrid
           setupMenu()
       }
   }

   fun setupMenu() {
       menuViewModel.menuMakanan.observe(requireActivity()){menuMakanan ->
           var isMakanan = true
           setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menuMakanan, isMakanan)
       }

       menuViewModel.menuMinuman.observe(requireActivity()){menuMinuman ->
           var isMakanan = false
           setupRecyclerViewMenu(SharedPreferencesManager.isGrid, menuMinuman, isMakanan)
       }
   }



   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
   }

   // bottomNavigationView jika ditaruh di onViewCreated() saat pertama kali dijalankan
   // akan bernilai null. Jadi kalau diakses selectedItemId nya aplikasi akan forceclose
   override fun onStart() {
       super.onStart()
       val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
       binding.btnChart.setOnClickListener {
           // Set item terpilih di BottomNavigationView
           bottomNavigationView.selectedItemId = R.id.cartFragment
       }

       binding.btnUser.setOnClickListener {
           // Set item terpilih di BottomNavigationView
           bottomNavigationView.selectedItemId = R.id.profileFragment
       }
   }

   fun setupMenuRekomendasi(menu: List<Any>) {
       var menuRekomendasi = menu as ArrayList<MenuItemEntity>

       val adapter = RekomendasiMenuAdapter(menuRekomendasi)
       binding.rvRekomendasiMenu.adapter = adapter
       binding.rvRekomendasiMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


       adapter.setOnItemClickListener { menuItem ->
//            Toast.makeText(requireContext(), menuItem.menu_name, Toast.LENGTH_SHORT).show()

           menuViewModel.setMenuDetail(menuItem)
           menuViewModel.getDataMenuDetail().observe(requireActivity()) {result ->
               Log.e("SimpleLiveData", result.toString())
           }
           val actionToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
           findNavController().navigate(actionToDetailFragment)
       }

   }


   fun setupRecyclerViewMenu(isGrid: Boolean, menus: List<Any>, isMakanan: Boolean) {
       SharedPreferencesManager.isGrid = isGrid
       val adapter = MenuAdapter(SharedPreferencesManager.isGrid, menus.filterNot { it is String })
       binding.btnChangeLayout.setImageResource(R.drawable.icon_list_layout)

       lateinit var recyclerView: RecyclerView

       if (isMakanan) {
           recyclerView = binding.rvMenuMakanan
           recyclerView.adapter = adapter
       } else {
           recyclerView = binding.rvMenuMinuman
           recyclerView.adapter = adapter
       }

       if (isGrid) {
           recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
       } else {
           recyclerView.layoutManager = LinearLayoutManager(requireContext())
       }

       adapter.setOnItemClickListener { menuItem ->
//            Toast.makeText(requireContext(), menuItem.menu_name, Toast.LENGTH_SHORT).show()

           menuViewModel.setMenuDetail(menuItem)
           menuViewModel.getDataMenuDetail().observe(requireActivity()) {result ->
               Log.e("SimpleLiveData", result.toString())
           }
           val actionToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
           findNavController().navigate(actionToDetailFragment)
       }
   }






   override fun onDestroy() {
       super.onDestroy()
       _binding = null
   }



    */