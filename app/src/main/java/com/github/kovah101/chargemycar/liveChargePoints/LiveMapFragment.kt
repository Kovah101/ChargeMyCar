package com.github.kovah101.chargemycar.liveChargePoints

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentLiveMapBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.loadAdBanner
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.github.kovah101.chargemycar.viewModel.ChargeQueryAPIStatus
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import timber.log.Timber


/**
 * Live Result of Charge Point Query in Map form
 */
class LiveMapFragment : Fragment(), OnMapReadyCallback {

    // Ad variables
    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentLiveMapBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_live_map, container, false
        )
        // has overflow menu to swap to list
        setHasOptionsMenu(true)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.liveMap)

        // get shared viewmodel & generate search query
        val livePointViewModel: ChargePointViewModel by activityViewModels()
        livePointViewModel.getChargePointQuery()

        // bind viewmodel to view
        binding.livePointsViewModel = livePointViewModel
        binding.lifecycleOwner = this

        // load advert banner
        adView = AdView(context as AppCompatActivity)
        binding.liveMapAdContainer.addView(adView)
        binding.liveMapAdContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadAdBanner(adView, livePointViewModel)
            }
        }

        // observe live list of charge points and generate map when it is
        livePointViewModel.listOfChargePoints.observe(viewLifecycleOwner, Observer {
            it?.let {
                when {
                    it.isNotEmpty() -> {
                        val mapFragment =
                            childFragmentManager.findFragmentById(R.id.liveMap) as SupportMapFragment
                        mapFragment.getMapAsync(this)
                    }
                }
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.live_map_list_menu, menu)
    }

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
        googleMap.isMyLocationEnabled = true

        // setup shared viewmodel
        val livePointsViewModel: ChargePointViewModel by activityViewModels()
        // create builder for marker locations + add user location
        val builder = LatLngBounds.builder()
        val myLocation =
            LatLng(livePointsViewModel.myLatitude.value!!, livePointsViewModel.myLongitude.value!!)
        if (livePointsViewModel.useLocation.value == true) {
            builder.include(myLocation)
        }
        // observe live charge points and create markers + add to builder
        livePointsViewModel.listOfChargePoints.observe(viewLifecycleOwner, Observer { list ->
            list?.forEach { chargePoint ->
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
        })
        // create camera animation for all markers
        val bounds = builder.build()
        val padding = 264
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)
    }

    private fun makeSmallMarker(chargePoint: ChargePoint): BitmapDescriptor {
        val height = 198
        val width = 144
        val icon: Bitmap
        if (chargePoint.chargePointStatus == "In service") {
            icon = BitmapFactory.decodeResource(resources, R.drawable.cp_map_icon)
            Timber.d("Out of Service!")
        } else {
            icon = BitmapFactory.decodeResource(resources, R.drawable.cp_map_icon_false)
        }
        val smallIcon = Bitmap.createScaledBitmap(icon, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(smallIcon)
    }

}