package com.github.kovah101.chargemycar.savedChargePoints

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.R.*
import com.github.kovah101.chargemycar.distanceColor
import com.github.kovah101.chargemycar.haversineDistance
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint

class ChargePointAdapter: RecyclerView.Adapter<ChargePointAdapter.ViewHolder>() {

    var data = listOf<ChargePoint>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var dummyUserLat = 51.4707
    var dummyUserLong = -0.1206

    override fun getItemCount() = data.size


    // Set all charge point data to specific views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.postcode.text = item.postcode
        // calculate actual distance from dummy user
        val trueDistance = haversineDistance(dummyUserLat, dummyUserLong, item.latitude, item.longitude)
        // round true distance to 2dp
        holder.distance.text = String.format("%.2f", trueDistance)
        // adjust colour appropriately, test with ID
        //holder.distance.background.
        holder.connectorType.text = item.connectorType
        holder.locationType.text = item.locationType

        // change status text color
        if (item.chargePointStatus){
            holder.status.setTextColor(res.getColor(color.green_500))
            holder.status.text = res.getString(R.string.inService)
        } else {
            holder.status.setTextColor(res.getColor(color.red))
            holder.status.text = res.getString(R.string.outService)
        }
        // checkbox maybe put in ViewModel?
        if (item.locationType.toInt() % 2 == 0 ){
            holder.favourite.isChecked = true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(layout.charge_point_list_item, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postcode : TextView = itemView.findViewById(id.postcode)
        val distance : TextView = itemView.findViewById(id.distance)
        val connectorType : TextView = itemView.findViewById(id.connectorType)
        val status : TextView = itemView.findViewById(id.status)
        val favourite: CheckBox = itemView.findViewById(id.favourite)
        val locationType: TextView = itemView.findViewById(id.locationType)
    }
}

