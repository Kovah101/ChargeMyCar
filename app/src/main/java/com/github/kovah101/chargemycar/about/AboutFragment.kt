package com.github.kovah101.chargemycar.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentAboutBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding
import com.github.kovah101.chargemycar.loadAdBanner
import com.github.kovah101.chargemycar.viewModel.ChargePointViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


/**
 * About fragment to describe the functionality of the app.
 */
class AboutFragment : Fragment() {

    // Ad variables
    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentAboutBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about, container, false)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.about)

        // shared viewmodel
        val livePointsViewModel: ChargePointViewModel by activityViewModels()

        // load advert banner
        adView = AdView(context as AppCompatActivity)
        binding.aboutAdContainer.addView(adView)
        binding.aboutAdContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadAdBanner(adView, livePointsViewModel)
            }
        }

        return binding.root
    }


}