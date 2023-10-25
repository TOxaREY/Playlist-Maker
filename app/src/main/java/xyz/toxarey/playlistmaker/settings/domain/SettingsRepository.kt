package xyz.toxarey.playlistmaker.settings.domain

interface SettingsRepository {
    fun getThemeIsDark(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}