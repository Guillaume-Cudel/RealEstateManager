package com.guillaume.project9.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillaume.project9.R
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import java.io.File
import java.lang.StringBuilder

class PropertyListAdapter(private val listener: Communicator): ListAdapter<Property, PropertyViewHolder>(PropertyViewHolder.PropertyComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder.create(parent)
    }

    //private var photosList: List<Photo?> = listOf()


    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        // todo get data and display them
        val context = holder.itemView.context
        val current = getItem(position)

        val price = current.price.toString()
        val str = StringBuilder(price)
        var idx: Int = str.length - 3
        while (idx > 0) {
            str.insert(idx, ".");
            idx -= 3;
        }
        val priceWithMonnayUnity = "$str €"
        holder.price.text = priceWithMonnayUnity

        holder.price.setTextColor(ContextCompat.getColor(context, R.color.overlay_light_primary))
        holder.kind.text = current.kind
        val surface = current.surface.toString()
        val surfaceText = "$surface m2"
        holder.surface.text = surfaceText
        holder.surface.setTextColor(ContextCompat.getColor(context, R.color.little_grey_bold))
        holder.city.text = current.cityAddress
        holder.date.text = current.launchOrSellDate


        if (current.photos.isNotEmpty()) {
            val photosString: String? = current.photos[0]
            val photoFile = photosString?.let { File(it) }
            val myBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            //holder.bind(current.launchOrSellDate)
            holder.date.text = current.launchOrSellDate
            Glide.with(context)
                .load(myBitmap)
                .centerCrop()
                .into(holder.photo)
        }

        holder.itemView.setOnClickListener { v ->
            /*val activity = v!!.context as AppCompatActivity
            val detailFragment = PropertyDetailFragment()
            listener.passData(current)

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.propertyListFragment, detailFragment)
                .addToBackStack(null).commit()*/
            listener.passData(current)
        }

        /*fun updatePhotos(photos: List<Photo?>){
        photosList = photos
        notifyDataSetChanged()
    }*/
    }
}


    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date: TextView = itemView.findViewById(R.id.property_launch_date)
        val photo: ImageView = itemView.findViewById(R.id.property_image)
        val price: TextView = itemView.findViewById(R.id.property_price)
        val kind: TextView = itemView.findViewById(R.id.property_kind)
        val surface: TextView = itemView.findViewById(R.id.property_area)
        val city: TextView = itemView.findViewById(R.id.property_location)


        /*fun bind(text: String?){
        propertyDate.text = text
    }*/

        companion object {
            fun create(parent: ViewGroup): PropertyViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_property, parent, false)
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

