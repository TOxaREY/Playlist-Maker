package xyz.toxarey.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import xyz.toxarey.playlistmaker.utils.DARK_THEME_KEY
import xyz.toxarey.playlistmaker.utils.PLAYLISTMAKER_PREFERENCES

class App: Application() {
    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(
            PLAYLISTMAKER_PREFERENCES,
            MODE_PRIVATE
        )
        darkTheme = sharedPrefs.getBoolean(
            DARK_THEME_KEY,
            false
        )
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
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