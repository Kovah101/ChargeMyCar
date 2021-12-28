package com.github.kovah101.chargemycar.savedChargePoints

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
import com.github.kovah101.chargemycar.databinding.FragmentSavedListBinding
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * Saved Charge points displayed in a list from database
 */
class SavedListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSavedListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_list, container, false
        )
        // has overflow menu to swap to map
        setHasOptionsMenu(true)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.savedList)

        // old separate viewmodel
        //application object for database
//        val application = requireNotNull(this.activity).application
//        // create view model & bind the UI to it
//        val dataSource = ChargeDatabase.getInstance(application).chargeDatabaseDAO
//        val viewModelFactory = SavedPointsViewModelFactory(dataSource, application)
//        val savedPointsViewModel =
//            ViewModelProvider(this, viewModelFactory)[SavedPointsViewModel::class.java]

        // shared viewmodel
        val savedPointsViewModel: ChargePointViewModel by activityViewModels()

        binding.savedPointsViewModel = savedPointsViewModel

        binding.lifecycleOwner = this

        // Add an Observer for the SnackBar variable to initiate message
        savedPointsViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared),
                    Snackbar.LENGTH_SHORT
                ).show()
                // Reset state so message only shown once
                savedPointsViewModel.doneShowingSnackBar()
            }
        })

        // create adapter and bind the data to it
        //
        val adapter =
            SavedPointAdapter(SavedPointAdapter.ChargePointListener { chargeLat, chargeLong ->
                Timber.d("Launching Google Maps Intent -> Lat:$chargeLat, Long:$chargeLong")
                launchMapDirections(chargeLat.toFloat(), chargeLong.toFloat())
            }, SavedPointAdapter.FavouriteListener { chargePoint, checked ->
                if (!checked) {
                    Timber.d("Remove Item ID: ${chargePoint.chargePointId} from Database")
                    savedPointsViewModel.findAndRemoveChargePoint(chargePoint.chargePointId)
                }

            })
        binding.chargeList.adapter = adapter

        // apply database as source for the adapter
        savedPointsViewModel.chargePoints.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.saved_list_map_menu, menu)
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