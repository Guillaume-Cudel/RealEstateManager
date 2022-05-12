package com.guillaume.project9.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.project9.R
import com.guillaume.project9.model.Property

class PropertyListAdapter: ListAdapter<Property, PropertyViewHolder>(PropertyViewHolder.PropertyComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.launchOrSellDate)
    }

}


class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    //todo get all id from items
    private val propertyDate: TextView = itemView.findViewById(R.id.property_launch_date)

    fun bind(text: String?){
        propertyDate.text = text
    }

    companion object{
        fun create(parent: ViewGroup): PropertyViewHolder{
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)
            return PropertyViewHolder(view)
        }
    }


    class PropertyComparator : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.cityAddress == newItem.cityAddress
        }
    }
}

