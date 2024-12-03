package com.example.fetch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val groupedItems: Map<Int, List<Item>>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val displayList: List<DisplayItem> = groupedItems.flatMap { (listId, items) ->
        listOf(DisplayItem.Header(listId)) + items.sortedBy { it.name }.map { DisplayItem.Data(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = when (viewType) {
            ViewType.HEADER.ordinal -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)
            ViewType.DATA.ordinal -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = displayList[position]
        when (item) {
            is DisplayItem.Header -> holder.headerTextView?.text = "List ID: ${item.listId}"
            is DisplayItem.Data -> holder.dataTextView?.text = item.item.name
        }
    }

    override fun getItemCount(): Int = displayList.size

    override fun getItemViewType(position: Int): Int {
        return when (displayList[position]) {
            is DisplayItem.Header -> ViewType.HEADER.ordinal
            is DisplayItem.Data -> ViewType.DATA.ordinal
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTextView: TextView? = view.findViewById(R.id.headerTextView)
        val dataTextView: TextView? = view.findViewById(R.id.dataTextView)
    }

    sealed class DisplayItem {
        data class Header(val listId: Int) : DisplayItem()
        data class Data(val item: Item) : DisplayItem()
    }

    private enum class ViewType {
        HEADER, DATA
    }
}
