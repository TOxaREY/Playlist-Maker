package xyz.toxarey.playlistmaker.app

import android.app.Application
import xyz.toxarey.playlistmaker.creator.Creator

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val settingsRepository = Creator.provideSettingsRepository(this)
        settingsRepository.switchTheme(settingsRepository.getThemeIsDark())
    }
}