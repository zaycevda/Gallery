package com.example.gallery.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.R
import com.example.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(
        vbFactory = ActivityMainBinding::bind,
        viewBindingRootId = R.id.container
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bnv
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(
            navigationBarView = bottomNavigationView,
            navController = navController
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.photosFragment,
                R.id.favoritesFragment -> binding.bnv.isGone = false

                else -> binding.bnv.isGone = true
            }
        }
    }
}