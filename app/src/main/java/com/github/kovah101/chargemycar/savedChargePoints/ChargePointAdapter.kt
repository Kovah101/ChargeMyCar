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
import com.github.kovah101.chargemycar.TextItemViewHolder
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint

class ChargePointAdapter: RecyclerView.Adapter<ChargePointAdapter.ViewHolder>() {

    var data = listOf<ChargePoint>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size


    // Set all charge point data to specific views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.postcode.text = item.postcode
        //val trueDistance = HaversineDistance(dummyUserLat, dummyUserLong, item.latitude, item.longitude)
        holder.distance.text = item.locationType
        //holder.distance.background.colorFilter = DistanceColor(trueDistance)
        holder.connectorType.text = item.connectorType

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
    }
}

