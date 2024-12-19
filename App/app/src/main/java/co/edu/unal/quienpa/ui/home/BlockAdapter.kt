package co.edu.unal.quienpa.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.unal.quienpa.R
import com.bumptech.glide.Glide
import co.edu.unal.quienpa.databinding.ItemBlockBinding
import co.edu.unal.quienpa.databinding.ItemHeaderBinding

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1

class BlockAdapter(
    private val items: List<Item>,
    private val clickListener: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BlockViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BlockViewHolder) {
            val item = items[position - 1] // Resta 1 porque el primer elemento es el header.
            holder.bind(item, clickListener)
        }
    }

    override fun getItemCount(): Int = items.size + 1 // Añade 1 para el  header

    class HeaderViewHolder(binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    class BlockViewHolder(private val binding: ItemBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, clickListener: (Item) -> Unit) {
            // Usa Glide para cargar la imagen desde la URL.
            Glide.with(binding.root.context)
                .load(item.imageUrl) // Carga la imagen desde la URL en el item.
                .into(binding.blockImage)


            binding.blockText.text = item.text
            binding.root.setOnClickListener { clickListener(item) }
        }
    }
}
