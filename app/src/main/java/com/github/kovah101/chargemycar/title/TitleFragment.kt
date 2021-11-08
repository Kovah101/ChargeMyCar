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
    //  1 - Link to Github (30 mins)
    //  2-Create Blank fragments & navigation including overflow menu & nav drawer(2.5 hours)
    //  3-Design Title Fragment (1 hour)
    //  4-Design about & options fragments (1 hour)
    //  5-Timber for Logs (10 minutes)
    //
    // TODO: Phase 2 - Database, ViewModel & RecyclerView
    // TODO: Phase 3 - Permissions, Real data & maps
    // TODO: Phase 4  - Polish & testing



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