package com.example.borrowbox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.borrowbox.R
import com.example.borrowbox.model.Item

class ItemsAdapter(
    private var items: List<Item>,
    private val onRequestClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvDesc: TextView = view.findViewById(R.id.tvItemDescription)
        val btnRequest: Button = view.findViewById(R.id.btnRequestBorrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvDesc.text = item.description

        if (item.available) {
            holder.btnRequest.text = "Request Borrow"
            holder.btnRequest.isEnabled = true
            holder.btnRequest.alpha = 1f
            holder.btnRequest.setOnClickListener { onRequestClick(item) }
        } else {
            holder.btnRequest.text = "Unavailable"
            holder.btnRequest.isEnabled = false
            holder.btnRequest.alpha = 0.5f
        }
    }

    override fun getItemCount() = items.size

    fun filter(query: String, original: List<Item>) {
        items = if (query.isBlank()) original
        else original.filter { it.name.contains(query, ignoreCase = true) }
        notifyDataSetChanged()
    }
}