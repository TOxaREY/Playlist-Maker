package xyz.toxarey.playlistmaker.settings.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository
import xyz.toxarey.playlistmaker.utils.DARK_THEME_KEY
import xyz.toxarey.playlistmaker.utils.PLAYLISTMAKER_PREFERENCES

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private var darkTheme = false
    private val sharedPrefs = context.getSharedPreferences(
        PLAYLISTMAKER_PREFERENCES,
        Context.MODE_PRIVATE
    )

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