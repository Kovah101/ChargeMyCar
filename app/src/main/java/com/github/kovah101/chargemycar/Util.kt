package com.github.kovah101.chargemycar

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint

/**
 * These functions create a formatted string that can be set in a TextView.
 */


fun formatChargePoints(chargePoints : List<ChargePoint>, resources : Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.text_title))
        chargePoints.forEach{
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

fun haversineDistance(userLat: Double, userLong: Double, pointLat: Float, pointLong: Float): Double {
    val earthRadius = 6371 // earth radius in km

    val dLat = Math.toRadians((pointLat.toDouble()-userLat))
    val dLong = Math.toRadians(pointLong.toDouble()-userLong)

    val startLat = Math.toRadians(userLat)
    val startLong = Math.toRadians(userLong)

    val a = haversine(dLat) + Math.cos(startLat)*Math.cos(pointLat.toDouble())*haversine(dLong)
    val c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a))

    return earthRadius * c
}

private fun haversine (angle : Double) : Double {
    return Math.pow(Math.sin(angle/2), 2.0)
}