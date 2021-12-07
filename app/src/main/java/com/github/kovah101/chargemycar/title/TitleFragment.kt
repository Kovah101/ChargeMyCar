package com.github.kovah101.chargemycar.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding


/**
 * Title Fragment to welcome user, show symbol
 * Buttons for: nearest Charge Points, nearest to postcode, saved list
 * Menu to navigate between fragments
 */
class TitleFragment : Fragment() {
    // TODO: Phase 1 - Setup, Navigation & Structure (Total 5 hours 10 mins)
    //  1- Link to Github (30 mins)
    //  2- Create Blank fragments & navigation including overflow menu & nav drawer(2.5 hours)
    //  3- Design Title Fragment (1 hour)
    //  4- Design about & options fragments (1 hour)
    //  5- Timber for Logs (10 minutes)

    // TODO: Phase 2 - Database & ViewModel (Total 4 hours 15 mins)
    //  1- ChargePoint Entity (40 mins)
    //  2- ChargePointDatabaseDAO (15 mins)
    //  3- Create + Test Room Database (15mins + 45mins)
    //  4- Add Favourite ViewModel + Add to savedList layout (30mins + 55mins)
    //  5- Coroutines for Database interaction (34mins)
    //  6- Adding & removing dummy data (20mins)

    // TODO: Phase 3 - RecyclerView + single item add/remove
    //  0- Setup (15mins)
    //  1- Create RecyclerView + Adapter + bindings (45 minutes +)
    //  2- Refactor ViewHolder methods
    //  3- Add Databinding & Custom Adapter
    //  4- Customise item View
    //  5- Add Click item function - to maps intent
    //  6- Add Click item function - add/remove single item

    // TODO: Phase 4 - Permissions, Real data & map fragments (BIG!!)
    // TODO: Phase 5  - Polish & testing



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title)

        // set button click listeners
        // Nearest live charge points
        binding.liveChargePoints.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_liveListFragment)
        )
        // Local live charge points
        binding.postcodeChargePoints.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_liveListFragment)
        )
        // Favourite charge points
        binding.favouriteChargePoints.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_savedListFragment)
        )

        return binding.root
    }


}