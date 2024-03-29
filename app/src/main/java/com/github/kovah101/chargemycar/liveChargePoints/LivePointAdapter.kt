package com.github.kovah101.chargemycar.liveChargePoints


import android.graphics.PorterDuff
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.kovah101.chargemycar.R.*
import com.github.kovah101.chargemycar.distanceColor
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.github.kovah101.chargemycar.databinding.ChargePointListItemBinding
import com.github.kovah101.chargemycar.databinding.LivePointListItemBinding
import com.github.kovah101.chargemycar.distanceToChargePoint


class LivePointAdapter(
    val userLat: Double?,
    val userLong: Double?,
    val clickListener: ChargePointListener,
    val favListener: FavouriteListener
) :
    ListAdapter<ChargePoint, LivePointAdapter.ViewHolder>(ChargePointDiffCallback()) {

    // Set all charge point data to specific views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, userLat, userLong, clickListener, favListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // find views and link them to item data
    // calculate distance from user and generate color
    // use companion object to hold the from function to create ViewHolder
    class ViewHolder private constructor(val binding: LivePointListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ChargePoint,
            userLat: Double?,
            userLong: Double?,
            clickListener: ChargePointListener,
            favListener: FavouriteListener
        ) {

            binding.chargePoint = item
            binding.clickListener = clickListener
            binding.favListener = favListener
            binding.executePendingBindings()
            // checkbox defaults to off in live list
            binding.favourite.isChecked = false

            // TODO: put into BindingUtils when using live location
            // calculate actual distance from dummy user
            val trueDistance = distanceToChargePoint(
                userLat,
                userLong,
                item.latitude.toDouble(),
                item.longitude.toDouble()
            )
            //haversineDistance(userLat, userLong, item.latitude.toFloat(), item.longitude.toFloat())
            // round true distance to 2dp
            binding.distance.text = String.format("%.2f", trueDistance)
            //adjust colour appropriately, test with ID
            val res = itemView.context.resources
            val colour = distanceColor((trueDistance))
            binding.distance.background.setColorFilter(
                res.getColor(colour),
                PorterDuff.Mode.SRC_ATOP
            )

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LivePointListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }


    // Callback for calculating the diff between two non-null items in a list.
    // Used by ListAdapter to calculate the minimum number of changes between and old list and a new
    // list that's been passed to `submitList`.
    class ChargePointDiffCallback : DiffUtil.ItemCallback<ChargePoint>() {
        override fun areItemsTheSame(oldItem: ChargePoint, newItem: ChargePoint): Boolean {
            return oldItem.chargePointId == newItem.chargePointId
        }

        override fun areContentsTheSame(oldItem: ChargePoint, newItem: ChargePoint): Boolean {
            return oldItem == newItem
        }
    }

    // Click listener for the map direction intent
    class ChargePointListener(val clickListener: (chargeLat: String, chargeLong: String) -> Unit) {
        fun onClick(chargePoint: ChargePoint) =
            clickListener(chargePoint.latitude, chargePoint.longitude)
    }

    // Click listener for add/remove from favourites
    class FavouriteListener(val favListener: (chargePoint: ChargePoint, checked: Boolean) -> Unit) {
        fun onClick(chargePoint: ChargePoint, checked: Boolean) = favListener(chargePoint, checked)
    }

}

