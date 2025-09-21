package com.example.moviles2primerparcial.ui.breedslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviles2primerparcial.R
import com.example.moviles2primerparcial.data.remote.dto.BreedDTO

/**
 * RecyclerView adapter responsible for:
 * - Holding a mutable list of breeds.
 * - Binding each item to a simple two-line row layout.
 * - Emitting click events via a lambda provided by the caller.
 */
class BreedAdapter(
    private val onClick: (BreedDTO) -> Unit
) : RecyclerView.Adapter<BreedAdapter.VH>() {

    private val items = mutableListOf<BreedDTO>()

    @SuppressLint("NotifyDataSetChanged") // Full refresh is acceptable for this use case.
    fun setData(newItems: List<BreedDTO>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_breed, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    /**
     * ViewHolder keeps references to row views to avoid repeated lookups.
     */
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)

        /**
         * Binds one BreedDTO to the row:
         * - Title shows the breed name.
         * - Subtitle shows the origin (with fallback to "-").
         */
        fun bind(b: BreedDTO) {
            tvTitle.text = b.name
            tvSubtitle.text = itemView.context.getString(
                R.string.breed_origin,
                b.origin ?: "-"
            )
        }
    }
}