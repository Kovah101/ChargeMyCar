package com.github.kovah101.chargemycar.savedChargePoints

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
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
import timber.log.Timber

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
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val postcode: TextView = itemView.findViewById(id.postcode)
        val distance: TextView = itemView.findViewById(id.distance)
        val connectorType: TextView = itemView.findViewById(id.connectorType)
        val status: TextView = itemView.findViewById(id.status)
        val favourite: CheckBox = itemView.findViewById(id.favourite)
        val locationType: TextView = itemView.findViewById(id.locationType)

        fun bind(item: ChargePoint, userLat: Double, userLong: Double) {
            val res = itemView.context.resources
            postcode.text = item.postcode
            // calculate actual distance from dummy user
            val trueDistance =
                haversineDistance(userLat, userLong, item.latitude, item.longitude)
            // round true distance to 2dp
            distance.text = String.format("%.2f", trueDistance)
            // adjust colour appropriately, test with ID
            val colour = distanceColor((item.locationType.toDouble() * 2.4))
            distance.background.setColorFilter(res.getColor(colour), PorterDuff.Mode.SRC_ATOP)
            connectorType.text = item.connectorType
            locationType.text = item.locationType

            // change status text color
            if (item.chargePointStatus) {
                status.setTextColor(res.getColor(color.green_500))
                status.text = res.getString(R.string.inService)
            } else {
                status.setTextColor(res.getColor(color.red))
                status.text = res.getString(R.string.outService)
            }
            // checkbox maybe put in ViewModel?
            if (item.locationType.toInt() % 2 == 0) {
                favourite.isChecked = true
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(layout.charge_point_list_item, parent, false)

                return ViewHolder(view)
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

