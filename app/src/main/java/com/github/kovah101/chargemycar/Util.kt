package com.github.kovah101.chargemycar

import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.kovah101.chargemycar.network.ChargeQueryPoint
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * These functions create a formatted string that can be set in a TextView.
 */


fun formatChargePoints(chargePoints: List<ChargePoint>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.text_title))
        chargePoints.forEach {
            append("<br>")
            append(resources.getString(R.string.ID))
            append("\t${it.chargePointId}<br>")
            append(resources.getString(R.string.latitude))
            append("\t${it.latitude}<br>")
            append(resources.getString(R.string.longitude))
            append("\t${it.longitude}<br>")
            append(resources.getString(R.string.postcode))
            append("\t${it.postcode}<br>")
            append(resources.getString(R.string.connector_type))
            append("\t${it.connectorType}<br>")
            append(resources.getString(R.string.charge_point_status))
            append("\t${it.chargePointStatus}<br>")
            append(resources.getString(R.string.location_type))
            append("\t${it.locationType}<br><br>")
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun nearestQueryString(userLat: Double, userLong: Double): String {
    val latString = userLat.toString()
    val longString = userLong.toString()
    return "lat/$latString/long/$longString"
}

fun postcodeQueryString(postcode: Editable): String {
    // convert to string, trim and remove spaces
    var postcodeQuery = postcode.toString().trim()
    postcodeQuery = postcodeQuery.replace(" ", "")
    // check if correct format else return error
    return postcodeQuery
}

fun distanceToChargePoint(
    userLat: Double?,
    userLong: Double?,
    pointLat: Double,
    pointLong: Double
): Float {
    val myLocation = Location("myLocation")
    myLocation.latitude = userLat!!
    myLocation.longitude = userLong!!
    val cpLocation = Location("cpLocation")
    cpLocation.latitude = pointLat
    cpLocation.longitude = pointLong

    return myLocation.distanceTo(cpLocation) / 1000 // in km
}

fun distanceColor(distance: Float): Int {
    var distColor = when (distance.roundToInt()) {
        in 0..2 -> R.color.distance0
        in 3..5 -> R.color.distance1
        in 6..9 -> R.color.distance2
        in 10..15 -> R.color.distance3
        in 16..22 -> R.color.distance4
        in 23..30 -> R.color.distance5
        else -> {
            if (distance.roundToInt() > 30) {
                R.color.distance6
            } else {
                R.color.red
            }
        }
    }

    return distColor
}

// takes list of charge query points and returns a list of charge points
fun convertChargePoints(chargeQueryPoints: List<ChargeQueryPoint>): List<ChargePoint> {
    var chargepoints = mutableListOf<ChargePoint>()
    // transfer the details to charge point object then add to the list
    chargeQueryPoints.forEach { cp ->
        val chargePoint = ChargePoint()
        chargePoint.latitude = cp.location.latitude
        chargePoint.longitude = cp.location.longitude
        chargePoint.postcode = cp.location.address.postcode.toString()
        chargePoint.connectorType = cp.connector[0].connectorType
        chargePoint.chargePointStatus = cp.connector[0].status
        chargePoint.locationType = cp.locationType

        chargepoints.add(chargePoint)
    }
    return chargepoints
}