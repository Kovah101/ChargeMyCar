package com.github.kovah101.chargemycar.savedChargePoints


import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.R.*
import com.github.kovah101.chargemycar.distanceColor
import com.github.kovah101.chargemycar.haversineDistance
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.github.kovah101.chargemycar.databinding.ChargePointListItemBinding


class ChargePointAdapter :
    ListAdapter<ChargePoint, ChargePointAdapter.ViewHolder>(ChargePointDiffCallback()) {

    var dummyUserLat = 51.4707
    var dummyUserLong = -0.1206


    // Set all charge point data to specific views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, dummyUserLat, dummyUserLong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // find views and link them to item data
    // calculate distance from user and generate color
    // use companion object to hold the from function to create ViewHolder
    class ViewHolder private constructor(val binding: ChargePointListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChargePoint, userLat: Double, userLong: Double) {
            val res = itemView.context.resources
            binding.postcode.text = item.postcode
            // calculate actual distance from dummy user
            val trueDistance =
                haversineDistance(userLat, userLong, item.latitude, item.longitude)
            // round true distance to 2dp
            binding.distance.text = String.format("%.2f", trueDistance)
            // adjust colour appropriately, test with ID
            val colour = distanceColor((item.locationType.toDouble() * 2.4))
            binding.distance.background.setColorFilter(res.getColor(colour), PorterDuff.Mode.SRC_ATOP)
            binding.connectorType.text = item.connectorType
            binding.locationType.text = item.locationType

            // change status text color
            if (item.chargePointStatus) {
                binding.status.setTextColor(res.getColor(color.green_500))
                binding.status.text = res.getString(R.string.inService)
            } else {
                binding.status.setTextColor(res.getColor(color.red))
                binding.status.text = res.getString(R.string.outService)
            }
            // checkbox maybe put in ViewModel?
            if (item.locationType.toInt() % 2 == 0) {
                binding.favourite.isChecked = true
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChargePointListItemBinding.inflate(layoutInflater, parent, false)

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

}

