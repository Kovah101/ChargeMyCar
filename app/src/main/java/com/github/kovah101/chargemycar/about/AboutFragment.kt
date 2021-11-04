package com.github.kovah101.chargemycar.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentAboutBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding


/**
 * About fragment to describe the functionality of the app.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAboutBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about, container, false)

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.about)

        return binding.root
    }


}