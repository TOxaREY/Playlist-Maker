package xyz.toxarey.playlistmaker.settings.domain

interface SettingsInteractor {
    fun getThemeIsDark(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}