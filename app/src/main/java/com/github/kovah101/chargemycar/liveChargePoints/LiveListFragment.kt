package com.github.kovah101.chargemycar.liveChargePoints

import android.content.Intent
import android.net.Uri
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
import com.github.kovah101.chargemycar.databinding.FragmentLiveListBinding
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import timber.log.Timber

/**
 * Live result of Charge Point Query in List form.
 */
class LiveListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentLiveListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_live_list, container, false
        )
        // has overflow menu to swap to map
        setHasOptionsMenu(true)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.liveList)

        // shared viewmodel
        val livePointsViewModel: ChargePointViewModel by activityViewModels()
        //val application = requireNotNull(this.activity).application
        // query for Charge Points on creation
        livePointsViewModel.getChargePointQuery()

        binding.livePointsViewModel = livePointsViewModel

        binding.lifecycleOwner = this

        // create adapter with maps intent and favourite handler
        val adapter = LivePointAdapter(livePointsViewModel.myLatitude.value,livePointsViewModel.myLongitude.value,LivePointAdapter.ChargePointListener { chargeLat, chargeLong ->
            Timber.d("Launching Google Maps Intent -> Lat:$chargeLat, Long:$chargeLong")
            launchMapDirections(chargeLat.toFloat(), chargeLong.toFloat())
        },LivePointAdapter.FavouriteListener { chargePoint, checked ->
            if (checked) {
                Timber.d("Add Item ID: ${chargePoint.chargePointId} from Database")
                livePointsViewModel.addIfNewChargePoint(chargePoint)
            }
        })
        // bind it to the live list
        binding.liveList.adapter = adapter


        // apply liveChargeList as source for the adapter
        livePointsViewModel.listOfChargePoints.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // look for changes to Query from options menu
        livePointsViewModel.limit.observe(viewLifecycleOwner, Observer {
            Timber.d("Limit has changed to: $it")
            livePointsViewModel.getChargePointQuery()
        })

        // look for changes to Query from options menu
        livePointsViewModel.distance.observe(viewLifecycleOwner, Observer {
            Timber.d("Distance has changed to: $it")
        })

        // look for changes to Query from title fragment
        // reload query if changed
        livePointsViewModel.postcode.observe(viewLifecycleOwner, Observer {
            Timber.d("Location string is: $it")
           // livePointsViewModel.getChargePointQuery()
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.live_list_map_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    // function takes charge point location and launches driving directions in the maps app of your choice
    private fun launchMapDirections(chargeLat : Float, chargeLong: Float){

        val gmmIntentUri = Uri.parse("google.navigation:q=$chargeLat,$chargeLong&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        val packageManager = requireContext().packageManager
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }

}