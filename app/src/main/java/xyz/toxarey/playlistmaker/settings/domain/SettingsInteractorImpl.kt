package xyz.toxarey.playlistmaker.settings.domain

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getThemeIsDark(): Boolean {
        return settingsRepository.getThemeIsDark()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        return settingsRepository.switchTheme(darkThemeEnabled)
    }
}