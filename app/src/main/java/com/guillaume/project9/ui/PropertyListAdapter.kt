package com.guillaume.project9.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillaume.project9.R
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import java.io.File

class PropertyListAdapter: ListAdapter<Property, PropertyViewHolder>(PropertyViewHolder.PropertyComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder.create(parent)
    }

    private var photosList: List<Photo?> = listOf()

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        // todo get data and display them
        val context = holder.itemView.context
        val current = getItem(position)

        val price = current.price.toString()
        holder.price.text = price
        holder.kind.text = current.kind
        holder.surface.text = current.surface.toString()
        holder.city.text = current.cityAddress
        holder.date.text = current.launchOrSellDate


        if(photosList.isNotEmpty()) {
            val photosString: String? = photosList[0]?.photos
            val photoFile = photosString?.let { File(it) }
            val myBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)


            //holder.bind(current.launchOrSellDate)
            holder.date.text = current.launchOrSellDate
            Glide.with(context)
                .load(myBitmap)
                .centerCrop()
                .into(holder.photo)
        }
    }

    fun updatePhotos(photos: List<Photo?>){
        photosList = photos
        notifyDataSetChanged()
    }
}





class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val date: TextView = itemView.findViewById(R.id.property_launch_date)
    val photo : ImageView = itemView.findViewById(R.id.property_image)
    val price: TextView = itemView.findViewById(R.id.property_price)
    val kind: TextView = itemView.findViewById(R.id.property_kind)
    val surface: TextView = itemView.findViewById(R.id.property_area)
    val city: TextView = itemView.findViewById(R.id.property_location)


    /*fun bind(text: String?){
        propertyDate.text = text
    }*/

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

