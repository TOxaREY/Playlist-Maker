package xyz.toxarey.playlistmaker.settings.data

import xyz.toxarey.playlistmaker.app.App
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val app: App): SettingsRepository {
    override fun getThemeIsDark(): Boolean {
        return app.darkTheme
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        app.switchTheme(darkThemeEnabled)
    }
}