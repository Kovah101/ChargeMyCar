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

        // use childFragmentManager instead of supportFragmentManager due to being in child fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.savedMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // shared viewmodel
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()

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
//        val centerLondon = LatLng(51.509865, -0.118092)
//        val towerLondon = LatLng(51.508530, -0.076132)
//        googleMap.isMyLocationEnabled = true
//        val myLat = googleMap.myLocation.latitude
//        val myLong = googleMap.myLocation.longitude
//        val myLocation = LatLng(myLat,myLong)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(48.8,2.35)))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(8f))
//        googleMap.addMarker(MarkerOptions().position(towerLondon).title("Tower of London"))

    }
}