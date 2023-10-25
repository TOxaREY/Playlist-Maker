package xyz.toxarey.playlistmaker.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.toxarey.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}