package xyz.toxarey.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    lateinit var darkThemeSwitch: androidx.appcompat.widget.SwitchCompat
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backToMainButton = findViewById<Button>(R.id.back_to_main_button_from_settings)
        darkThemeSwitch = findViewById(R.id.dark_theme_switch)
        val shareAppButton = findViewById<ImageView>(R.id.share_app_image_view)
        val supportButton = findViewById<ImageView>(R.id.support_image_view)
        val termsOfUseButton = findViewById<ImageView>(R.id.terms_of_use_image_view)

        backToMainButton.setOnClickListener {
            val backToMainIntent = Intent(this, MainActivity::class.java)
            startActivity(backToMainIntent)
            finish()
        }
        checkDarkTheme()
        darkThemeSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        shareAppButton.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_address))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
        }
        supportButton.setOnClickListener {
            val supportIntent = Intent()
            supportIntent.action = Intent.ACTION_SENDTO
            supportIntent.data = Uri.parse("mailto:" + getString(R.string.my_email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_email))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_email))
            startActivity(Intent.createChooser(supportIntent, getString(R.string.write_to_support)))
        }
        termsOfUseButton.setOnClickListener {
            val termsOfUseIntent = Intent()
            termsOfUseIntent.action = Intent.ACTION_VIEW
            termsOfUseIntent.data = Uri.parse(getString(R.string.terms_of_use_text))
            startActivity(Intent.createChooser(termsOfUseIntent, getString(R.string.terms_of_use)))
        }
    }
    private fun checkDarkTheme() {
        darkThemeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
}