package binar.academy.challengebinar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.databinding.RekomendasiItemMenuBinding

class RekomendasiMenuAdapter(val data: ArrayList<MenuItemEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: ((MenuItemEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (MenuItemEntity) -> Unit) {
        onItemClick = listener
    }

    // Membuat Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RekomendasiItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RekomendasiMenuHolder(binding)
    }

    // Memberitahu banyaknya data yang akan ditampilkan
    override fun getItemCount(): Int {
        return data.size
    }

    // Melakukan penentuan data yang akan ditampilkan pada setiap item / baris
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rekomendasiMenuHolder = holder as RekomendasiMenuHolder
        rekomendasiMenuHolder.onBind(data[position])
    }

    inner class RekomendasiMenuHolder(val binding: RekomendasiItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {


        fun onBind(data: MenuItemEntity) {
            binding.menuName.text = data.menu_name
            binding.imgPreview.setImageResource(data.menu_preview)

            binding.cardMenuItem.setOnClickListener {
                onItemClick?.invoke(data)
            }

        }
    }


}

