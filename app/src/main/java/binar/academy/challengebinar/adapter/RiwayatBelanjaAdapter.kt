package binar.academy.challengebinar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengebinar.data.CartDetailWithCartAndMenu
import binar.academy.challengebinar.data.CartEntity
import binar.academy.challengebinar.databinding.ItemRiwayatHeaderBinding
import binar.academy.challengebinar.databinding.RvRingkasanBinding
import binar.academy.challengebinar.databinding.RvRiwayatBinding
import binar.academy.challengebinar.utils.Pembayaran
import binar.academy.challengebinar.utils.Pengiriman
import binar.academy.challengebinar.utils.formatSeparator
import java.lang.IllegalArgumentException

class RiwayatBelanjaAdapter(val data: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_BELANJA = 1
        private const val ITEM_RINGKASAN = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]) {
            is String -> ITEM_HEADER
            is CartDetailWithCartAndMenu -> ITEM_BELANJA
            is CartEntity -> ITEM_RINGKASAN
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_HEADER -> {
                val binding = ItemRiwayatHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RiwayatHeaderHolder(binding)
            }
            ITEM_BELANJA -> {
                val binding = RvRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RiwayatItemHolder(binding)
            }
            ITEM_RINGKASAN -> {
                val binding = RvRingkasanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RingkasanItemHolder(binding)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            ITEM_HEADER -> {
                val headerHolder = holder as RiwayatHeaderHolder
                headerHolder.bindContent(data[position] as String)
            }
            ITEM_BELANJA -> {
                val riwayatHolder = holder as RiwayatItemHolder
                riwayatHolder.onBind(data[position] as CartDetailWithCartAndMenu)
            }
            ITEM_RINGKASAN -> {
                val ringkasanHolder = holder as RingkasanItemHolder
                ringkasanHolder.onBind(data[position] as CartEntity)
            }
        }
    }

    inner class RiwayatItemHolder(val binding: RvRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {
        val catatan = binding.catatan

        fun onBind(data: CartDetailWithCartAndMenu) {
            binding.namaMenu.text = data.menuItemEntity.menu_name
            binding.imgPreview.setImageResource(data.menuItemEntity.menu_preview)
            binding.tvSubTotal.text = "Rp " + data.cartDetailEntity.subTotal.formatSeparator().toString()
            binding.tvCount.text = data.cartDetailEntity.quantity.toString()
            binding.hargaMenu.text = "Rp " + data.menuItemEntity.menu_price.formatSeparator().toString()
            catatan.setText("${data.cartDetailEntity.catatan}")

            catatan.isEnabled = false

        }
    }

    inner class RiwayatHeaderHolder(val binding: ItemRiwayatHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindContent(text: String) {
            binding.textHeader.text = text
        }
    }

    inner class RingkasanItemHolder(val binding: RvRingkasanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: CartEntity) {
            binding.tvTotalHarga.text = "Rp " + data.totalPrice.formatSeparator().toString()
            binding.tvMetodePembayaran.text = if (data.metodePembayaran == Pembayaran.TUNAI) {
                    "Tunai"
                } else if (data.metodePembayaran == Pembayaran.DIGITAL) {
                    "Dompet Digital"
                } else {
                    "Undefined"
                }
            binding.tvMetodePengiriman.text = if (data.metodePengiriman == Pengiriman.AMBIL_SENDIRI) {
                "Ambil Sendiri"
            } else if (data.metodePengiriman == Pengiriman.DIKIRIM) {
                "Dompet Digital"
            } else {
                "Undefined"
            }
        }
    }
}