package xyz.toxarey.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import xyz.toxarey.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsActivityViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsActivityViewModel.getViewModelFactory()
        )[SettingsActivityViewModel::class.java]

        viewModel.currentThemeIsDark.observe(this) {checked ->
            binding.darkThemeSwitch.isChecked = checked
        }

        binding.darkThemeSwitch.isChecked = viewModel.getCurrentThemeIsDark()

        binding.backToMainButtonFromSettings.setOnClickListener {
            finish()
        }

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareAppFrameLayout.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeToSupportFrameLayout.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.termsOfUseFrameLayout.setOnClickListener {
            viewModel.termsOfUseFrame()
        }
    }
}