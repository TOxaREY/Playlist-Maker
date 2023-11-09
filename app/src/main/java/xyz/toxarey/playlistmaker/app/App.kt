package xyz.toxarey.playlistmaker.app

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import xyz.toxarey.playlistmaker.di.dataKoinModule
import xyz.toxarey.playlistmaker.di.interactorKoinModule
import xyz.toxarey.playlistmaker.di.repositoryKoinModule
import xyz.toxarey.playlistmaker.di.viewModelKoinModule
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataKoinModule,
                interactorKoinModule,
                repositoryKoinModule,
                viewModelKoinModule
            )
        }
        val settingsRepository: SettingsRepository by inject()
        settingsRepository.switchTheme(settingsRepository.getThemeIsDark())
    }
}