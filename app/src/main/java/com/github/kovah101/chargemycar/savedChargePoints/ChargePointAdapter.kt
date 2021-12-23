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
import com.github.kovah101.chargemycar.generated.callback.OnClickListener


class ChargePointAdapter (val clickListener : ChargePointListener, val favListener : FavouriteListener) :
    ListAdapter<ChargePoint, ChargePointAdapter.ViewHolder>(ChargePointDiffCallback()) {

    var dummyUserLat = 51.4707
    var dummyUserLong = -0.1206


    // Set all charge point data to specific views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, dummyUserLat, dummyUserLong, clickListener, favListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // find views and link them to item data
    // calculate distance from user and generate color
    // use companion object to hold the from function to create ViewHolder
    class ViewHolder private constructor(val binding: ChargePointListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChargePoint, userLat: Double, userLong: Double, clickListener: ChargePointListener, favListener: FavouriteListener) {

            binding.chargePoint = item
            binding.clickListener = clickListener
            binding.favListener = favListener
            binding.executePendingBindings()
            // checkbox maybe put in ViewModel? Maybe move to Binding Util? maybe not as it needs database checking
            // in favourites so checkbox starts true
            binding.favourite.isChecked = true

            // TODO: put into BindingUtils when using live location - add distance field to ChargePoint and propagate change
             //adjust colour appropriately, test with ID
//            val res = itemView.context.resources
//            val colour = distanceColor((item.locationType.toDouble() * 2.4))
//            binding.distance.background.setColorFilter(res.getColor(colour), PorterDuff.Mode.SRC_ATOP)
            // calculate actual distance from dummy user
            val trueDistance =
                haversineDistance(userLat, userLong, item.latitude, item.longitude)
            // round true distance to 2dp
            binding.distance.text = String.format("%.2f", trueDistance)
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

    // Click listener for the map direction intent
    class ChargePointListener(val clickListener: (chargeLat: Float, chargeLong: Float) -> Unit) {
        fun onClick(chargePoint: ChargePoint) = clickListener(chargePoint.latitude, chargePoint.longitude)
    }

    // Click listener for add/remove from favourites
    class FavouriteListener(val favListener:(chargePoint: ChargePoint, checked : Boolean) -> Unit) {
        fun onClick(chargePoint: ChargePoint, checked: Boolean) = favListener(chargePoint, checked)
    }

}

