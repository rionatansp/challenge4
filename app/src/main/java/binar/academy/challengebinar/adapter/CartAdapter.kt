package binar.academy.challengebinar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengebinar.data.CartDetailWithCartAndMenu
import binar.academy.challengebinar.databinding.RvCartBinding
import binar.academy.challengebinar.utils.formatSeparator

class CartAdapter(isFromKonfirmasiPesanan: Boolean = false) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onBtnPlusClick: ((Int, Int, Int) -> Unit)? = null
    private var onBtnMinClick: ((Int, Int, Int) -> Unit)? = null
    private var onCatatanUpdate: ((Int, String) -> Unit)? = null
    private var onBtnDeleteClick: ((Int) -> Unit)? = null
    private var isFromKonfirmasiPesanan = isFromKonfirmasiPesanan

    private val diffCallback = object : DiffUtil.ItemCallback<CartDetailWithCartAndMenu>() {
        override fun areItemsTheSame(
            oldItem: CartDetailWithCartAndMenu,
            newItem: CartDetailWithCartAndMenu
        ): Boolean {
            return oldItem.cartDetailEntity == newItem.cartDetailEntity
        }

        override fun areContentsTheSame(
            oldItem: CartDetailWithCartAndMenu,
            newItem: CartDetailWithCartAndMenu
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<CartDetailWithCartAndMenu>) = differ.submitList(value)


    fun setOnBtnPlusClickListener(listener: (Int, Int, Int) -> Unit) {
        onBtnPlusClick = listener
    }

    fun setOnBtnMinClickListener(listener: (Int, Int, Int) -> Unit) {
        onBtnMinClick = listener
    }

    fun setOnBtnCatatanClickListener(listener: (Int, String) -> Unit) {
        onCatatanUpdate = listener
    }

    fun setOnBtnDeleteClickListener(listener: (Int) -> Unit) {
        onBtnDeleteClick = listener
    }



    // Membuat Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RvCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartHolder(binding)
    }

    // Memberitahu banyaknya data yang akan ditampilkan
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Melakukan penentuan data yang akan ditampilkan pada setiap item / baris
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartHolder = holder as CartHolder
        val data = differ.currentList[position]
        cartHolder.onBind(data)
    }

    inner class CartHolder(val binding: RvCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val catatan = binding.catatan
        val btnDelete = binding.btnDelete
        val btnPlus = binding.btnPlusUpdate
        val btnMin = binding.btnMinUpdate
        val btnCatatan = binding.btnUpdateCatatan

        fun onBind(data: CartDetailWithCartAndMenu) {
            binding.namaMenu.text = data.menuItemEntity.menu_name
            binding.imgPreview.setImageResource(data.menuItemEntity.menu_preview)
            binding.subTotal.text = "Rp " + data.cartDetailEntity.subTotal.formatSeparator().toString()
            binding.tvCount.text = data.cartDetailEntity.quantity.toString()
            catatan.setText("${data.cartDetailEntity.catatan}")

            if (isFromKonfirmasiPesanan) {
                catatan.isEnabled = false
                btnDelete.isEnabled = false
                btnDelete.visibility = View.INVISIBLE
                btnPlus.isEnabled = false
                btnMin.isEnabled = false
                btnCatatan.isClickable = false
                btnCatatan.visibility = View.GONE

            } else {
                btnPlus.setOnClickListener {
                    onBtnPlusClick?.invoke(data.cartDetailEntity.cart_detail_id, data.cartDetailEntity.quantity, data.menuItemEntity.menu_price)
                }

                btnMin.setOnClickListener {
                    onBtnMinClick?.invoke( data.cartDetailEntity.cart_detail_id, data.cartDetailEntity.quantity, data.menuItemEntity.menu_price)
                }

                btnCatatan.setOnClickListener {
                    onCatatanUpdate?.invoke(data.cartDetailEntity.cart_detail_id, binding.catatan.text.toString())
                }

                btnDelete.setOnClickListener {
                    onBtnDeleteClick?.invoke(data.cartDetailEntity.cart_detail_id)
                }
            }
        }
    }


}