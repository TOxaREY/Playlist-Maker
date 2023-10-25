package xyz.toxarey.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import xyz.toxarey.playlistmaker.creator.Creator
import xyz.toxarey.playlistmaker.app.App
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractor
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor

class SettingsActivityViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {
    private val darkThemeEnabled = MutableLiveData<Boolean>()
    val currentThemeIsDark: LiveData<Boolean> = darkThemeEnabled

    init {
        darkThemeEnabled.postValue(settingsInteractor.getThemeIsDark())
    }

    fun getCurrentThemeIsDark(): Boolean {
        return settingsInteractor.getThemeIsDark()
    }

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
        darkThemeEnabled.postValue(checked)
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                SettingsActivityViewModel(
                    Creator.provideSettingsInteractor(app),
                    Creator.provideSharingInteractor(app)
                )
            }
        }
    }
}