package com.github.kovah101.chargemycar.title

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.postcodeChecker
import com.github.kovah101.chargemycar.postcodeQueryString
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
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

    // MVP in 26 hours 20m

    // TODO: Phase 5 - Geo Permissions & map fragments (Total 10 hours 10m)
    //  0- Setup (20m)
    //  1- Edit Title fragment to enable postcode or fake live location query (2hr)
    //  2- Add Geo-permissions and use true location (1hr 45m)
    //  3 - Maps Setup - add sdk, set up cloud console, add API key (50m)
    //  4- Create SavedMap Layout (25m)
    //  5- Add Saved Points to SavedMap, define zoom & center on average, icon and onClick method + add user location to title fragment (3hr 30m)
    //  6- Create LiveMap Layout & add Live Points to LiveMap, define zoom, icon and onClick method, center on user, fixed straight to map bug with default useLocation in title (50m)
    //  7- create postcode checker and formatter (30m)
    //  8- Options menu accuracy/power option (20m)

    // TOTAL TIME: 36 hours 30m

    // TODO: Phase 5  - Polish & testing, remove add data from saved list, test large lists for null point errors in query result, change charge point lat & long to doubles, custom map info windows?

    private var userLat = 0.0
    private var userLong = -0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        // instance of the location provider client
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)

        // shared viewmodel
        val livePointsViewModel: ChargePointViewModel by activityViewModels()

        binding.livePointsViewModel = livePointsViewModel

        binding.lifecycleOwner = this

        // register the permissions callback and handle the users response
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    Timber.d("Fine Location Permission has been granted")
                } else {
                    binding.permissionsRequest.visibility = View.VISIBLE
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                activity as AppCompatActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // App has permission
                // find current location and display it in log
                // different priority types for different power + accuracy demands
                val cancelTokenSource = CancellationTokenSource()
                fusedLocationClient.getCurrentLocation(
                    livePointsViewModel.locationPriority,
                    cancelTokenSource.token
                ).addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLat = location.latitude
                        userLong = location.longitude
                        Timber.d("Users location is: $userLat,$userLong")
                        livePointsViewModel.myLatitude.value = userLat
                        livePointsViewModel.myLongitude.value = userLong
                    } else { // TODO deal with null - sandwich bar pop up & refactor duplicate code in buttons
                        Timber.d("location is null, may be turned off in settings")
                    }
                }
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // explain to the user why app needs fine location to find nearest charge points
                binding.permissionsRequest.visibility = View.VISIBLE
            }
            else -> { // directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        // default to using live location
        livePointsViewModel.useLocation.value = true

        // set button click listeners
        // Nearest live charge points
        binding.liveChargePoints.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_titleFragment_to_liveListFragment)
        }

        // Local live charge points
        binding.postcodeChargePoints.setOnClickListener { view ->
            // change to using postcode mode
            livePointsViewModel.useLocation.value = false
            // remove spaces from input string
            val postcodeString = postcodeQueryString(binding.Postcode.text)
            val correctPostcode = postcodeChecker(postcodeString)
            if (correctPostcode) {
                livePointsViewModel.postcode.value = postcodeString
                view.findNavController().navigate(R.id.action_titleFragment_to_liveListFragment)
            } else {
                // centralised snackbar message
                val snackMessage = Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.postcodeWrong),
                    Snackbar.LENGTH_SHORT
                )
                val snackView = snackMessage.view
                val snackText =
                    snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                snackText.gravity = Gravity.CENTER_HORIZONTAL
                snackText.textAlignment = View.TEXT_ALIGNMENT_CENTER
                snackMessage.show()
            }
        }

        // Favourite charge points
        binding.favouriteChargePoints.setOnClickListener { view ->
            // confirm to viewmodel we are using live location
            livePointsViewModel.useLocation.value = true
            view.findNavController().navigate(R.id.action_titleFragment_to_savedListFragment)
        }

        // Allow permissions button
        binding.allowPermission.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            binding.permissionsRequest.visibility = View.GONE
        }

        // Deny permissions button
        binding.denyPermission.setOnClickListener {
            binding.permissionsRequest.visibility = View.GONE
        }

        return binding.root
    }


}