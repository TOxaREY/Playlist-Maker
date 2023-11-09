package xyz.toxarey.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import org.koin.core.component.KoinComponent
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository
import xyz.toxarey.playlistmaker.utils.DARK_THEME_KEY

class SettingsRepositoryImpl: SettingsRepository, KoinComponent {
    private var darkTheme = false
    private val sharedPrefs: SharedPreferences = getKoin().get()

    override fun getThemeIsDark(): Boolean {
        return sharedPrefs.getBoolean(
            DARK_THEME_KEY,
            false
        )
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        if (darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        sharedPrefs.edit().putBoolean(
            DARK_THEME_KEY,
            darkTheme
        ).apply()
    }
}