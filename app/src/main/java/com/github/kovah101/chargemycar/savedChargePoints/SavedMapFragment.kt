package com.github.kovah101.chargemycar.savedChargePoints

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentSavedMapBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Saved Charge Points displayed on a map from database.
 */

// TODO: Add sharedViewModel, center map on user, add saved charge points, customise map icon

// implements OnMapReadyCallback
class SavedMapFragment : Fragment(), OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSavedMapBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_map, container, false
        )
        // has overflow menu to swap to list
        setHasOptionsMenu(true)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.savedMap)

        // shared viewmodel
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()

        val savedChargePoints = savedPointsViewModel.chargePoints

        // use childFragmentManager instead of supportFragmentManager due to being in child fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.savedMap) as SupportMapFragment
        mapFragment.getMapAsync(this)




        binding.lifecycleOwner = this

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.saved_map_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // center map on London TODO:  center on average location add more options and animations
        var centerLat = 0.0
        var centerLong = 0.0
        // add markers for each saved charge point
        // TODO customise icon + add onClick behaviour
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()
        val savedChargePoints = savedPointsViewModel.chargePoints.value
        savedChargePoints?.forEach { chargePoint ->
            centerLat += chargePoint.latitude.toDouble()
            centerLong += chargePoint.longitude.toDouble()
            val cpLocation =
                LatLng(chargePoint.latitude.toDouble(), chargePoint.longitude.toDouble())
            val cpName = chargePoint.postcode
            googleMap.addMarker(
                MarkerOptions().position(cpLocation)
                    .title(cpName)
            )
        }
        if (savedChargePoints != null) {
            centerLat /= savedChargePoints.size
            centerLong /= savedChargePoints.size
        } else {
            centerLat = 51.509865
            centerLong = -0.118092
        }
        val centerLondon = LatLng(centerLat, centerLong)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLondon, 14f))
    }
}