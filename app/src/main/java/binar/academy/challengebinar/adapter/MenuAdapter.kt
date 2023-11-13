package binar.academy.challengebinar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengebinar.data.MenuItemEntity
import binar.academy.challengebinar.databinding.GridItemMenuBinding
import binar.academy.challengebinar.databinding.ItemRiwayatHeaderBinding
import binar.academy.challengebinar.databinding.LinearItemMenuBinding
import binar.academy.challengebinar.utils.formatSeparator
import java.lang.IllegalArgumentException

class MenuAdapter(val isGrid: Boolean, val data: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: ((MenuItemEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (MenuItemEntity) -> Unit) {
        onItemClick = listener
    }

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_MENU = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is String -> ITEM_HEADER
            is MenuItemEntity -> ITEM_MENU
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }


    // Membuat Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            ITEM_HEADER -> {
//                val binding = ItemMenuHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                return MenuHeaderHolder(binding)
//            }
//            ITEM_MENU -> {
//                if (isGrid) {
//                    val binding = GridItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                    return GridMenuHolder(binding)
//                }
//                else {
//                    val binding = LinearItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                    return LinearMenuHolder(binding)
//                }
//            }
//            else -> throw IllegalArgumentException("Undefined view type")
//        }

        if (isGrid) {
            val binding = GridItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GridMenuHolder(binding)
        }
        else {
            return when(viewType) {
                ITEM_HEADER -> {
                    return MenuHeaderHolder(ItemRiwayatHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
                ITEM_MENU -> {
                    return LinearMenuHolder(LinearItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                } else -> throw IllegalArgumentException("Undefined view type")
            }
        }
    }




    // Memberitahu banyaknya data yang akan ditampilkan
    override fun getItemCount(): Int {
        return data.size
    }

    // Melakukan penentuan data yang akan ditampilkan pada setiap item / baris
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder.itemViewType) {
//            ITEM_HEADER -> {
//                val headerHolder = holder as MenuHeaderHolder
//                headerHolder.bindContent(data[position] as String)
//            }
//            ITEM_MENU -> {
//                if (isGrid) {
//                    val gridHolder = holder as GridMenuHolder
//                    gridHolder.onBind(data[position] as MenuItem)
//                } else {
//                    val linearMenuHolder = holder as LinearMenuHolder
//                    linearMenuHolder.onBind(data[position] as MenuItem)
//                }
//            }
//        }

        if (isGrid) {
            val gridHolder = holder as GridMenuHolder
            gridHolder.onBind(data[position] as MenuItemEntity)
        } else {
            when (holder.itemViewType) {
                ITEM_HEADER -> {
                    val headerHolder = holder as MenuHeaderHolder
                    headerHolder.bindContent(data[position] as String)
                }
                ITEM_MENU -> {
                    val linearMenuHolder = holder as LinearMenuHolder
                    linearMenuHolder.onBind(data[position] as MenuItemEntity)
                }
            }
        }
    }
    inner class LinearMenuHolder(val binding: LinearItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {


        fun onBind(data: MenuItemEntity) {
            binding.imgPreview.setImageResource(data.menu_preview)
            binding.namaMenu.text = data.menu_name
            binding.hargaMenu.text = "Rp. ${data.menu_price.formatSeparator()}"
            val deskripsi = data.menu_description.split(" ").take(6).joinToString(" ")
            binding.deskripsiMenu.text = "${deskripsi} ...."

            binding.cardMenuItem.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    inner class GridMenuHolder(val binding: GridItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: MenuItemEntity) {
            binding.menuName.text = data.menu_name
            binding.hargaMenu.text = "Rp. ${data.menu_price.formatSeparator()}"
            binding.imgPreview.setImageResource(data.menu_preview)

            binding.cardMenuItem.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    inner class MenuHeaderHolder(val binding: ItemRiwayatHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindContent(text: String) {
            binding.textHeader.text = text
        }
    }
}




