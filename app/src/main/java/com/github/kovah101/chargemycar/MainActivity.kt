package com.github.kovah101.chargemycar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.github.kovah101.chargemycar.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create binding object to access UI
        @Suppress("UNUSED VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        // find navController and link to Action Bar
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        // set action bar title
        this.supportActionBar?.setTitle(R.string.title)

        Timber.i("Timber is all setup")
    }

    // make the back button on Action Bar to navigate up
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}