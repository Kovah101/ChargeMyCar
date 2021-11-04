package com.github.kovah101.chargemycar.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.kovah101.chargemycar.R
import com.github.kovah101.chargemycar.databinding.FragmentOptionsBinding
import com.github.kovah101.chargemycar.databinding.FragmentTitleBinding

/**
 * Options for Charge Point Query, saved to preferences
 */
class OptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentOptionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_options, container, false
        )

        // set action bar title
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.options)

        return binding.root
    }


}