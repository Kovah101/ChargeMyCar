package com.github.kovah101.chargemycar.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.nearestQueryString
import com.github.kovah101.chargemycar.postcodeQueryString
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import timber.log.Timber


/**
 * Title Fragment to welcome user, show symbol
 * Buttons for: nearest Charge Points, nearest to postcode, saved list
 * Menu to navigate between fragments
 */
class TitleFragment : Fragment() {
    // TODO: Phase 1 - Setup, Navigation & Structure (Total 5 hours 10m)
    //  1- Link to Github (30 mins)
    //  2- Create Blank fragments & navigation including overflow menu & nav drawer(2.5 hours)
    //  3- Design Title Fragment (1 hour)
    //  4- Design about & options fragments (1 hour)
    //  5- Timber for Logs (10 minutes)

    // TODO: Phase 2 - Database & ViewModel (Total 4 hours 15m)
    //  1- ChargePoint Entity (40 mins)
    //  2- ChargePointDatabaseDAO (15 mins)
    //  3- Create + Test Room Database (15mins + 45mins)
    //  4- Add Favourite ViewModel + Add to savedList layout (30mins + 55mins)
    //  5- Coroutines for Database interaction (34mins)
    //  6- Adding & removing dummy data (20mins)

    // TODO: Phase 3 - RecyclerView + single item add/remove + Map Intent (Total 7 hours 45m)
    //  0- Setup (15m)
    //  1- Create RecyclerView + Adapter + bindings (1hr)
    //  2- Design custom list item (35m)
    //  3- Add DataBinding & Custom Adapter + HaversineDistance & DistanceColor Utils function (2hr 45)
    //  4- Refactor onBindViewHolder & onCreateViewHolder (15m+15m)
    //  5- Add Click item function - to maps intent (+45m + 25m)
    //  6- Add Click item function - remove single item from favourites(15m +30m)

    // TODO: Phase 4 - Internet Permissions & Real data (Total 8 hours 55m)
    //  0- Setup (30m)
    //  1- Create LiveList layout & viewmodel + factory + convert ot shared viewmodel architecture - (1h 15m + 45m)
    //  2- Add retrofit API service + connect to the internet + display JSON string (1h 10m)
    //  3- Convert Data class to take in JSON too,Parse The JSON response & display the size/list of charge points (1h)
    //  4- Coroutines to streamline retrofit API service (45m)
    //  5- Display detailed list of live charge points (30m) + Favourite star sort (15m) + Added Android 11+ Permissions (20m)
    //  6- Create Options layout to adjust URI search parameters - Distance & Limit DONE (30m+ 60m+ 15m)
    //  7- Add Error handling on QueryAPI through livedata enum that controls view visibility (45m+25m)

    // TODO: Phase 5 - Geo Permissions & map fragments (estimate 8 hours)
    //  0- Setup (20m)
    //  1- Edit Title fragment to enable postcode or fake live location query (40m+30m+15m) - no rigorous postcode test yet! although may come up as error in livelist?
    //  2- Add Geo-permissions and use true location
    //  3- Create SavedMap Layout
    //  4- Add Saved Points to SavedMap, define zoom, icon and onClick method
    //  5- Create LiveMap Layout
    //  6- Add Live Points to LiveMap, define zoom, icon and onClick method
    //  7- create postcode checker and formatter
    //  8-

    // TODO: Phase 5  - Polish & testing, test large lists for null point errors in query result, change charge point lat & long to doubles

    val dummyUserLat = 51.4707
    val dummyUserLong = -0.1206

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false
        )

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title)

        // shared viewmodel
        val livePointsViewModel: ChargePointViewModel by activityViewModels()

        binding.livePointsViewModel = livePointsViewModel

        binding.lifecycleOwner = this


        // set button click listeners
        // Nearest live charge points
        binding.liveChargePoints.setOnClickListener { view ->
            val nearestString = nearestQueryString(dummyUserLat, dummyUserLong)
            livePointsViewModel.location.value = nearestString
            view.findNavController().navigate(R.id.action_titleFragment_to_liveListFragment)
        }

        // Local live charge points
        binding.postcodeChargePoints.setOnClickListener { view ->
            val postcodeString = postcodeQueryString(binding.Postcode.text)
            //TODO Add postcode checker/ formatter
            livePointsViewModel.location.value = postcodeString
            view.findNavController().navigate(R.id.action_titleFragment_to_liveListFragment)
        }

        // Favourite charge points
        binding.favouriteChargePoints.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_savedListFragment)
        )


        return binding.root
    }


}