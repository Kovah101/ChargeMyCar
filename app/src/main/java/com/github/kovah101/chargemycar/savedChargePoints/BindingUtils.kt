package com.github.kovah101.chargemycar.savedChargePoints

import android.graphics.PorterDuff
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.distanceColor
import com.github.kovah101.chargemycar.haversineDistance
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.github.kovah101.chargemycar.viewModel.ChargeQueryAPIStatus


@BindingAdapter("distance")
fun TextView.setDistance(item: ChargePoint?){
    item?.let {
//        val trueDistance =
//            haversineDistance(userLat, userLong, item.latitude, item.longitude)
//        text = String.format("%.2f", trueDistance)
        // adjust colour appropriately, test with ID
//        val colour = distanceColor((item.locationType.toDouble() * 2.4))
//        background.setColorFilter (context.resources.getColor(colour), PorterDuff.Mode.SRC_ATOP)
    }
}

@BindingAdapter("postcode")
fun TextView.setPostcode(item: ChargePoint?){
    item?.let {
        text = item.postcode
    }
}

@BindingAdapter("locationType")
fun TextView.setLocationType(item: ChargePoint?){
    item?.let {
        text = item.locationType
    }
}

@BindingAdapter("connectorType")
fun TextView.setConnectorType(item: ChargePoint?){
    item?.let {
        text = item.connectorType
    }
}

@BindingAdapter("status")
fun TextView.setStatus(item: ChargePoint?){
    item?.let {
        if (item.chargePointStatus == "In service"){
            setTextColor(context.resources.getColor(R.color.green_500))
            text = context.resources.getString(R.string.inService)
        } else {
            setTextColor(context.resources.getColor(R.color.red))
            text = context.resources.getString(R.string.outService)
        }
    }
}

@BindingAdapter("chargeQueryApiStatus")
fun bindStatus(view: View, status: ChargeQueryAPIStatus?){
    when(status){
        ChargeQueryAPIStatus.DONE -> {
            when(view.id){
                R.id.liveList -> view.visibility = View.VISIBLE
                R.id.chargeString -> view.visibility = View.GONE
                R.id.statusImage -> view.visibility = View.GONE
            }
        }
        ChargeQueryAPIStatus.ERROR -> {
            when(view.id){
                R.id.liveList -> view.visibility = View.GONE
                R.id.chargeString -> view.visibility = View.VISIBLE
                R.id.statusImage -> view.visibility = View.VISIBLE
            }
        }
        ChargeQueryAPIStatus.LOADING -> {
            when(view.id){
                R.id.liveList -> view.visibility = View.GONE
                R.id.chargeString -> view.visibility = View.VISIBLE
                R.id.statusImage -> view.visibility = View.VISIBLE

            }
        }
    }
}

@BindingAdapter("queryApiStatusImage")
fun bindImage(view: ImageView, status: ChargeQueryAPIStatus?){
    when(status){
        ChargeQueryAPIStatus.LOADING ->
            view.setImageResource(R.drawable.loading_animation)
        ChargeQueryAPIStatus.ERROR ->
            view.setImageResource(R.drawable.connection_error)
        ChargeQueryAPIStatus.DONE -> Unit
    }
}
