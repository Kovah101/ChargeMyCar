package com.github.kovah101.chargemycar.savedChargePoints

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentSavedMapBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.loadAdBanner
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import timber.log.Timber

/**
 * Saved Charge Points displayed on a map from database.
 */

// TODO: Add sharedViewModel, center map on user, add saved charge points, customise map icon

// implements OnMapReadyCallback
class SavedMapFragment : Fragment(), OnMapReadyCallback {

    // Ad variables
    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentSavedMapBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_map, container, false
        )
        // has overflow menu to swap to list
        setHasOptionsMenu(true)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.savedMap)

        // shared viewModel
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()
        binding.savedPointsViewModel = savedPointsViewModel
        binding.lifecycleOwner = this

// load advert banner
        adView = AdView(context as AppCompatActivity)
        binding.savedMapAdContainer.addView(adView)
        binding.savedMapAdContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadAdBanner(adView, savedPointsViewModel)
            }
        }

        // map fragment initialised after charge points have been observed
        savedPointsViewModel.chargePoints.observe(viewLifecycleOwner, Observer {
            Timber.d(" 1 - map points observed")
            // use childFragmentManager instead of supportFragmentManager due to being in child fragment
            val mapFragment = childFragmentManager.findFragmentById(R.id.savedMap) as SupportMapFragment
            mapFragment.getMapAsync(this)
        })

        return binding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.saved_map_list_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
       // setup map fragment controls
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled = true
        // center map on London TODO:  clean up average position
        var centerLat = 0.0
        var centerLong = 0.0
        val builder = LatLngBounds.builder()
        // add markers for each saved charge point
        // TODO custom info window for colourblind + onClick??
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()

        val savedChargePoints = savedPointsViewModel.chargePoints.value
        savedChargePoints?.forEach { chargePoint ->
//            centerLat += chargePoint.latitude.toDouble()
//            centerLong += chargePoint.longitude.toDouble()
            val smallMarker = makeSmallMarker(chargePoint)
            val cpLocation =
                LatLng(chargePoint.latitude.toDouble(), chargePoint.longitude.toDouble())
            val cpName = chargePoint.postcode
            googleMap.addMarker(
                MarkerOptions().position(cpLocation)
                    .title(cpName)
                    .snippet(chargePoint.connectorType)
                    .icon(smallMarker)
            )
            builder.include(cpLocation)
        }
        if (savedChargePoints != null) {
//            centerLat /= savedChargePoints.size
//            centerLong /= savedChargePoints.size
            Timber.d("map points - center on: $centerLat, $centerLong")
        } else {
//            centerLat = 51.509865
//            centerLong = -0.118092
            Timber.d("chargePoints == null map points on: $centerLat, $centerLong")
        }
//        val centerMap = LatLng(centerLat, centerLong)
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, 14f))
        val bounds = builder.build()
        val padding = 264
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)
    }

    private fun makeSmallMarker(chargePoint: ChargePoint): BitmapDescriptor {
        val height = 198
        val width = 144
        var icon = BitmapFactory.decodeResource(resources,R.drawable.cp_map_icon)
        if(chargePoint.chargePointStatus == "In service") {
             icon = BitmapFactory.decodeResource(resources,R.drawable.cp_map_icon)
            Timber.d("Out of Service!")
        } else {
            icon = BitmapFactory.decodeResource(resources,R.drawable.cp_map_icon_false)
        }
        val smallIcon = Bitmap.createScaledBitmap(icon,width,height, false)
        return BitmapDescriptorFactory.fromBitmap(smallIcon)
    }
}