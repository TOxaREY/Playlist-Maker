package xyz.toxarey.playlistmaker.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.ActivityRootBinding

class RootActivity: AppCompatActivity() {
    private var binding: ActivityRootBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding!!.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment, R.id.newPlaylistFragment -> {
                    binding!!.bottomNavigationView.visibility = View.GONE
                    binding!!.lineAboveBottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding!!.bottomNavigationView.visibility = View.VISIBLE
                    binding!!.lineAboveBottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}