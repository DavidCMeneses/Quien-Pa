package co.edu.unal.quienpa.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.unal.quienpa.databinding.ItemBlockBinding
import co.edu.unal.quienpa.databinding.ItemHeaderBinding

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1

class BlockAdapter(private val items: List<Item>, private val clickListener: (Item) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            val item = items[position - 1]  // Restamos 1 porque el primer elemento es el encabezado
            holder.bind(item, clickListener)
        }
    }

    override fun getItemCount(): Int = items.size + 1 // Añadimos 1 por el encabezado

    class HeaderViewHolder(binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    class BlockViewHolder(private val binding: ItemBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, clickListener: (Item) -> Unit) {
            binding.blockImage.setImageResource(item.imageRes)
            binding.blockText.text = item.text
            binding.root.setOnClickListener { clickListener(item) }
        }
    }
}
