package xyz.toxarey.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractor
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor

class SettingsFragmentViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {
    private val _darkThemeEnabled = MutableLiveData<Boolean>()
    val currentThemeIsDark: LiveData<Boolean> = _darkThemeEnabled

    init {
        _darkThemeEnabled.postValue(settingsInteractor.getThemeIsDark())
    }

    fun getCurrentThemeIsDark(): Boolean {
        return settingsInteractor.getThemeIsDark()
    }

    fun switchTheme(checked: Boolean) {
        _darkThemeEnabled.postValue(checked)
        settingsInteractor.switchTheme(checked)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.writeToSupport()
    }

    fun termsOfUseFrame() {
        sharingInteractor.termsOfUse()
    }
}