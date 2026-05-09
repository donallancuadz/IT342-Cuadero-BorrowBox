package com.example.borrowbox.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.borrowbox.R
import com.example.borrowbox.model.BorrowRequestResponse

class RequestsAdapter(private val items: List<BorrowRequestResponse>) :
    RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvItemName.text = item.itemName
        holder.tvDate.text = item.requestDate
        holder.tvStatus.text = item.status

        val (bgColor, textColor) = when (item.status.uppercase()) {
            "ACTIVE"    -> Pair("#D4F5E2", "#1A7A44")
            "APPROVED"  -> Pair("#D4F5E2", "#1A7A44")
            "PENDING"   -> Pair("#E8E8E8", "#555555")
            "RETURNED"  -> Pair("#E8EBF5", "#0A1045")
            "REJECTED"  -> Pair("#FDE8E8", "#C0392B")
            else        -> Pair("#E8E8E8", "#555555")
        }

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 20f
            setColor(Color.parseColor(bgColor))
        }
        holder.tvStatus.background = drawable
        holder.tvStatus.setTextColor(Color.parseColor(textColor))
    }

    override fun getItemCount() = items.size
}