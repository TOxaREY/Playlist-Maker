package xyz.toxarey.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backToMainButton = findViewById<Button>(R.id.back_to_main_button)

        backToMainButton.setOnClickListener {
            val backToMainIntent = Intent(this, MainActivity::class.java)
            startActivity(backToMainIntent)
            finish()
        }
    }
}