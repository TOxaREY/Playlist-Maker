package xyz.toxarey.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.toxarey.playlistmaker.media_library.MediaLibraryActivity
import xyz.toxarey.playlistmaker.search.ui.SearchActivity
import xyz.toxarey.playlistmaker.settings.ui.SettingsActivity
import xyz.toxarey.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val searchIntent = Intent(
                this,
                SearchActivity::class.java
            )
            startActivity(searchIntent)
        }

        binding.mediaLibraryButton.setOnClickListener {
            val mediaLibraryIntent = Intent(
                this,
                MediaLibraryActivity::class.java
            )
            startActivity(mediaLibraryIntent)
        }

        binding.settingsButton.setOnClickListener {
            val settingsIntent = Intent(
                this,
                SettingsActivity::class.java
            )
            startActivity(settingsIntent)
        }
    }
}