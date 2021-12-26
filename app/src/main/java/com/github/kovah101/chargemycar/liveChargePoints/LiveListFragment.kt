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
import com.github.kovah101.chargemycar.savedChargePoints.ChargePointAdapter
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
        // query for Charge Points on creation
        livePointsViewModel.getChargePointQuery()

        // observe the success of the Charge Point Query
        // display recyclerView & charge points if successful
        // display textView with error message
        livePointsViewModel.success.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.chargeString.visibility = View.GONE
                binding.liveList.visibility = View.VISIBLE
            }
        })

        binding.livePointsViewModel = livePointsViewModel

        binding.lifecycleOwner = this

        // create adapter with maps intent and favourite handler
        val adapter = ChargePointAdapter(ChargePointAdapter.ChargePointListener { chargeLat, chargeLong ->
            Timber.d("Launching Google Maps Intent -> Lat:$chargeLat, Long:$chargeLong")
            launchMapDirections(chargeLat.toFloat(), chargeLong.toFloat())
        },ChargePointAdapter.FavouriteListener { chargePoint, checked ->
            if (!checked) {
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